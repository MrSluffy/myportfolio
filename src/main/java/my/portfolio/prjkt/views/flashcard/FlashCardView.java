package my.portfolio.prjkt.views.flashcard;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.dialog.DialogVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import my.portfolio.prjkt.data.entities.FlashCard;
import my.portfolio.prjkt.data.entities.MyUser;
import my.portfolio.prjkt.data.services.impl.DeviceServiceImp;
import my.portfolio.prjkt.data.services.impl.FlashCardServiceImp;
import my.portfolio.prjkt.data.services.impl.MyUserServiceImp;
import my.portfolio.prjkt.exceptions.AuthException;
import my.portfolio.prjkt.views.MainLayout;

import java.util.List;
import java.util.stream.Collectors;


@PageTitle("Flash Card")
@Route(value = "project/flashcard", layout = MainLayout.class)
public class FlashCardView extends Main implements HasComponents, HasStyle {

    private OrderedList flashList;
    private final FlashCardServiceImp serviceImp;
    private final MyUserServiceImp myUserServiceImp;
    private final DeviceServiceImp device;

    Dialog formDialog = new Dialog();

    TextField titleField = new TextField();
    TextArea descriptionField = new TextArea();
    TextField urlField = new TextField();

    TextField answerField = new TextField("Answer");
    TextField questionField = new TextField("Question");


    Button add = new Button("Add");

    Button btnadd = new Button("Add");

    Select<String> sortBy = new Select<>();



    public FlashCardView(FlashCardServiceImp serviceImp, MyUserServiceImp myUserServiceImp, DeviceServiceImp device) {
        this.serviceImp = serviceImp;
        this.myUserServiceImp = myUserServiceImp;
        this.device = device;

        constructUI();

        configureFlashes();

        configureDialog();

    }

    private void configureDialog() {
        VerticalLayout dialogLayout = createDialogLayout(formDialog);
        formDialog.add(dialogLayout);
        formDialog.setMinWidth(device.isMobile() ? "80%" : "33%");
        formDialog.setModal(false);
        formDialog.setDraggable(true);
        formDialog.addThemeVariants(DialogVariant.LUMO_NO_PADDING);
        formDialog.getElement().setAttribute("aria-label", "Create new flashcard");

    }


    private VerticalLayout createDialogLayout(Dialog formDialog) {
        H3 dialogTitle = new H3("Create new flashcard");
        dialogTitle.addClassName("dialog-title");

        Header header = new Header(dialogTitle);
        header.setWidthFull();
        header.addClassNames("border-b", "border-contrast-10", "box-border", "flex", "items-center", "w-full");

        VerticalLayout createFields = createFlashField();

        VerticalLayout scrollContent = new VerticalLayout(createFields);

        Scroller scroller = new Scroller(scrollContent);

        Footer footer = createFooter(formDialog);
        VerticalLayout dialogContent = new VerticalLayout();

        header.setHeight("90px");
        dialogContent.addClassName("db-dialog");
        footer.setHeight("70px");
        footer.setWidthFull();
        scroller.setHeight("400px");
        footer.addClassNames("bg-contrast-5", "flex", "items-center", "w-full");
        dialogContent.add(header, scroller, footer);
        dialogContent.setSpacing(false);
        dialogContent.setPadding(false);
        dialogContent.getStyle().remove("width");
        dialogContent.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogContent.setClassName("dialog-no-padding-example-overlay");


        return dialogContent;
    }

    private VerticalLayout createFlashField() {
        titleField.setWidthFull();
        titleField.setLabel("Title");
        titleField.setRequired(true);

        answerField.setWidthFull();
        answerField.setRequired(true);

        questionField.setWidthFull();
        questionField.setRequired(true);

        descriptionField.setWidthFull();
        descriptionField.setLabel("Details");
        descriptionField.setRequired(true);
        descriptionField.setMaxLength(500);
        descriptionField.setValueChangeMode(ValueChangeMode.EAGER);
        descriptionField.addValueChangeListener(e -> e.getSource().setHelperText(e.getValue().length() + "/" + 500));

        urlField.setWidthFull();
        urlField.setRequired(true);
        urlField.setLabel("Source");
        urlField.setHelperText("Example: https://www.github.com/source");

        VerticalLayout section = new VerticalLayout(titleField,
                descriptionField, urlField, questionField, answerField);
        section.setPadding(false);
        section.setSpacing(false);
        section.setAlignItems(FlexComponent.Alignment.STRETCH);
        section.getElement().setAttribute("role", "region");
        return section;
    }


    private Footer createFooter(Dialog dialog) {

        add.addClassName("btn-dialog");

        add.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        add.addClickListener(buttonClickEvent -> {
            addNewFlashCard(titleField.getValue(), descriptionField.getValue(), urlField.getValue(), answerField.getValue(), questionField.getValue());
        });
        Button cancelButton = new Button("Cancel", e -> dialog.close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        HorizontalLayout buttonLayout = new HorizontalLayout(cancelButton,
                add);
        buttonLayout.setWidthFull();
        buttonLayout
                .setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        buttonLayout.addClassName("footer");

        return new Footer(buttonLayout);
    }


    private void addNewFlashCard(String title, String detail, String reference, String answer, String question) {
        try {
            serviceImp.save(title, detail, reference, answer, question);
            formDialog.close();
            flashList.removeAll();
            configureFlashes();
        } catch (AuthException e) {
            Notification.show("You need to sign in as Guest or Create a new account",
                    5000,
                    Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }


    public void configureFlashes() {

        List<FlashCard> flashCardList = serviceImp.findAll(sortBy.getValue());

        for(FlashCard flashCard : flashCardList){

            flashList.add(new FlashCardListItem(
                    flashCard.getId(),
                    flashCard.getCardTitle(),
                    flashCard.getCardDetail(),
                    flashCard.getCardReference(),
                    flashCard.getCardDate(),
                    flashCard.getCarqQuestion(),
                    flashCard.getCardAnswer(),
                    flashCard.getMyUserInFlashCard().getUserName(),
                    flashCard.isCorrect(),
                    flashCard.getCardNumber(),
                    flashCard.getUserListCorrectAnswer()
                            .stream()
                            .map(MyUser::getUserName)
                            .collect(Collectors.toList()),
                    device,
                    serviceImp));
        }

        btnadd.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        btnadd.addClickListener( event -> formDialog.open());

        Icon icon = new Icon(VaadinIcon.PLUS);
        icon.addClassName("btn-icon");
        icon.addClassNames("size-l");

        var btn = new Button(icon);

        btn.addClassNames("bg-contrast-5", "flex", "flex-col", "items-start", "p-m", "rounded-l");
        btn.setHeightFull();
        btn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ICON);
        btn.addClickListener(event -> formDialog.open());
        flashList.add(btn);
        btnadd.setVisible(serviceImp.flashCardCount() > 4);
        btn.setVisible(serviceImp.flashCardCount() <= 4);
        sortBy.setVisible(serviceImp.flashCardCount() > 2);
    }

    private void constructUI() {
        addClassNames("image-list-view", "pb-l", "px-l");
        addClassName("view-card");
        HorizontalLayout container = new HorizontalLayout();

        VerticalLayout headerContainer = new VerticalLayout();
        var con = new HorizontalLayout();
        con.addClassNames("items-center", "justify-between");
        con.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        con.setWidthFull();
        Label header = new Label("Flashes");
        Paragraph sub = new Paragraph("Review your knowledge instantly");
        sub.addClassName("sub-header");
        sub.addClassNames("text-xl");

        header.addClassName("header-h2");
        header.addClassNames("mb-0", "mt-l", "text-3xl");

        flashList = new OrderedList();
        flashList.addClassNames("gap-m", "grid", "list-none", "m-0", "p-0");
        flashList.addClassName("flashcard");

        container.add(header);
        con.add(sortBy, btnadd);
        headerContainer.setSpacing(false);
        headerContainer.setPadding(false);
        headerContainer.add(container, sub, con);

        add(headerContainer, flashList);

        sortBy.setLabel("Sort by");
        sortBy.setItems("Ascending", "Descending", "Default");
        sortBy.setValue("Default");
        sortBy.addValueChangeListener( e-> {
            flashList.removeAll();
            configureFlashes();
        });
    }
}

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
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import my.portfolio.prjkt.data.entities.FlashCard;
import my.portfolio.prjkt.data.services.impl.DeviceServiceImp;
import my.portfolio.prjkt.data.services.impl.FlashCardServiceImp;
import my.portfolio.prjkt.data.services.impl.MyUserServiceImp;
import my.portfolio.prjkt.views.MainLayout;


@PageTitle("Flash Card")
@Route(value = "project/flashcard", layout = MainLayout.class)
public class FlashCardView extends Main implements HasComponents, HasStyle {

    private OrderedList flashList;
    private final FlashCardServiceImp serviceImp;
    private MyUserServiceImp myUserServiceImp;
    private DeviceServiceImp device;

    Dialog formDialog = new Dialog();

    TextField titleField = new TextField();
    TextArea descriptionField = new TextArea();
    TextField urlField = new TextField();

    TextField answerField = new TextField("Answer");
    TextField questionField = new TextField("Question");


    Button add = new Button("Add");


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
        urlField.setHelperText("https://www.github.com/source");

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
        serviceImp.save(title, detail, reference, answer, question);
        formDialog.close();
        flashList.removeAll();
        configureFlashes();
    }


    public void configureFlashes() {

        for(FlashCard flashCard : serviceImp.findAllCards()){
            flashList.add(new FlashCardListItem(
                    flashCard.getId(),
                    flashCard.getCardTitle(),
                    flashCard.getCardDetail(),
                    flashCard.getCardReference(),
                    flashCard.getCardDate(),
                    flashCard.getCarqQuestion(),
                    flashCard.getCardAnswer(),
                    serviceImp));
        }

        Icon icon = new Icon(VaadinIcon.PLUS);
        icon.addClassName("btn-icon");
        icon.addClassNames("size-l");

        var btn = new Button(icon);

        btn.addClassNames("bg-contrast-5", "flex", "flex-col", "items-start", "p-m", "rounded-l");
        btn.setHeightFull();
        btn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ICON);
        btn.addClickListener(event -> {
            formDialog.open();
        });
        flashList.add(btn);
    }

    private void constructUI() {
        addClassNames("image-list-view", "max-w-screen-lg", "mx-auto", "pb-l", "px-l");
        HorizontalLayout container = new HorizontalLayout();
        container.addClassNames("items-center", "justify-between");

        VerticalLayout headerContainer = new VerticalLayout();
        H2 header = new H2("Flashes");
        header.addClassName("header-h2");
        header.addClassNames("mb-0", "mt-l", "text-3xl");
        headerContainer.add(header);

        flashList = new OrderedList();
        flashList.addClassNames("gap-m", "grid", "list-none", "m-0", "p-0");
        container.add(header);
        add(container, flashList);

    }
}

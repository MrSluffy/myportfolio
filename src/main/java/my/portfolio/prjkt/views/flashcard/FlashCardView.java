package my.portfolio.prjkt.views.flashcard;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.dialog.DialogVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.html.OrderedList;
import com.vaadin.flow.component.html.Section;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import my.portfolio.prjkt.data.entities.FlashCard;
import my.portfolio.prjkt.data.services.impl.FlashCardServiceImp;
import my.portfolio.prjkt.views.MainLayout;


@PageTitle("Flash Card")
@Route(value = "project/flashcard", layout = MainLayout.class)
public class FlashCardView extends Main implements HasComponents, HasStyle {

    private OrderedList flashList;
    private final FlashCardServiceImp serviceImp;


    Dialog formDialog = new Dialog();

    TextField titleField = new TextField();
    TextArea descriptionField = new TextArea();
    TextField urlField = new TextField();

    TextField answerField = new TextField("Answer");
    TextField questionField = new TextField("Question");


    Button add = new Button("Add");

    VerticalLayout layout = new VerticalLayout();


    public FlashCardView(FlashCardServiceImp serviceImp) {
        this.serviceImp = serviceImp;

        constructUI();

        configureFlashes();

        VerticalLayout dialogLayout = createDialogLayout(formDialog);
        dialogLayout.addClassNames("flex", "items-start", "p-l", "rounded-l");
        formDialog.add(dialogLayout);


    }


    private VerticalLayout createDialogLayout(Dialog formDialog) {

        layout.addClassName("flash-dialog");

        H2 headline = new H2("Add new flashcard");

        headline.addClassName("flash-item-title");

        Button close = new Button(new Icon(VaadinIcon.CLOSE_SMALL));

        close.addClickListener(event -> formDialog.close());

        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        HorizontalLayout header = new HorizontalLayout(headline, close);
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidthFull();
        Section section = new Section();
        section.addClassNames("border-b", "border-contrast-10", "box-border", "flex", "h-xl", "items-end",
                "w-full");
        section.setWidthFull();
        layout.setWidthFull();

        section.add(header);


        formDialog.setModal(false);
        formDialog.setDraggable(true);
        formDialog.addThemeVariants(DialogVariant.LUMO_NO_PADDING);
        formDialog.setMaxHeight("80%");
        formDialog.setMinWidth("33%");

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

        add.addClassName("btn-dialog");

        add.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        add.addClickListener(buttonClickEvent -> {
            addNewFlashCard(titleField.getValue(), descriptionField.getValue(), urlField.getValue(), answerField.getValue(), questionField.getValue());
        });
        var hl = new HorizontalLayout();
        hl.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        hl.setWidthFull();
        hl.add(add);


        layout.add(section, titleField, descriptionField, urlField, questionField, answerField, hl);

        return layout;
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
        btn.addClickListener(event ->
                formDialog.open());
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

package my.portfolio.prjkt.views.flashcard;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.dialog.DialogVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import my.portfolio.prjkt.data.services.impl.DeviceServiceImp;
import my.portfolio.prjkt.data.services.impl.FlashCardServiceImp;
import my.portfolio.prjkt.exceptions.AuthException;

public class FlashCardListItem extends ListItem {

    Dialog formDialog = new Dialog();

    Dialog itemLayout = new Dialog();

    TextField titleField = new TextField();
    TextArea descriptionField = new TextArea();
    TextField urlField = new TextField();

    TextField answerField = new TextField("Answer");
    TextField questionField = new TextField("Question");

    Button save = new Button("Save");
    private final Integer id;
    private final String title;
    private final String detail;
    private final String reference;
    private final String question;
    private final String answer;
    private final String username;
    private boolean isCorrect;
    private final int flashCardNumber;
    private DeviceServiceImp device;
    private final FlashCardServiceImp service;

    HorizontalLayout horlayout = new HorizontalLayout();

    Icon checked;

    public FlashCardListItem(Integer id,
                             String title,
                             String detail,
                             String reference,
                             String date,
                             String question,
                             String answer,
                             String username,
                             boolean isCorrect,
                             int flashCardNumber,
                             DeviceServiceImp device,
                             FlashCardServiceImp service) {
        this.id = id;
        this.title = title;
        this.detail = detail;
        this.reference = reference;
        this.question = question;
        this.answer = answer;
        this.username = username;
        this.isCorrect = isCorrect;
        this.flashCardNumber = flashCardNumber;
        this.device = device;
        this.service = service;
        addClassNames("bg-contrast-5", "flex", "flex-col", "items-start", "p-m", "rounded-l");
        addClassName("material-list");
        this.checked = createIcon(VaadinIcon.CHECK, "", "");
        horlayout.addClassNames("flex", "items-start");
        horlayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        horlayout.setWidthFull();


        configureDialog();

        VerticalLayout zomLayout = createLayout(itemLayout);
        zomLayout.addClassNames("flex", "items-start", "p-l", "rounded-l");
        itemLayout.add(zomLayout);

        ComponentEventListener<ClickEvent<MenuItem>> listener = e -> formDialog.open();

        ComponentEventListener<ClickEvent<MenuItem>> listenerDel = e -> {
            service.delete(id);
            UI.getCurrent().getPage().reload();
        };


        Span header = new Span();
        header.addClassNames("text-xl", "font-semibold");
        header.setText(title);
        header.add(" #" + flashCardNumber);
        MenuBar menuBar = new MenuBar();
        menuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY_INLINE);
        menuBar.setOpenOnHover(true);
        MenuItem item = createIconItem(menuBar, VaadinIcon.ELLIPSIS_DOTS_V, "more");
        SubMenu subMenu = item.getSubMenu();
        subMenu.addItem("Edit", listener);
        subMenu.addItem("Delete", listenerDel);
        subMenu.add(new Hr());
        subMenu.addItem("Report");
        horlayout.add(header, checked);

        checked.setVisible(isCorrect);

        Span subtitle = new Span();

        var anchorLayour = new HorizontalLayout();
        anchorLayour.addClassNames("flex", "wrap", "items-start");
        anchorLayour.setWidthFull();
        anchorLayour.setMargin(false);
        anchorLayour.setPadding(false);


        Anchor anchor = new Anchor("", "Source");
        anchor.addClassNames("text-s");
        anchor.setHref(reference);

        anchorLayour.add(anchor);


        subtitle.addClassNames("text-s", "text-secondary");
        subtitle.setText(date);

        Paragraph paragraph = new Paragraph(detail);

        paragraph.addClassName("my-m");

        addClickListener(event ->{
            itemLayout.open();
        });

        var bottomLayout = new HorizontalLayout();
        bottomLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        bottomLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.END);
        bottomLayout.setWidthFull();
        Span user = new Span();

        user.addClassNames("text-m", "text-secondary");
        user.setText("Author : " + username);


        bottomLayout.add(user, menuBar);


        add(horlayout, subtitle, anchorLayour, paragraph, bottomLayout);

    }

    private void configureDialog() {
        VerticalLayout dialogLayout = createDialogLayout(formDialog);
        formDialog.add(dialogLayout);
        formDialog.setModal(false);
        formDialog.setMinWidth(device.isMobile() ? "80%" : "33%");
        formDialog.setDraggable(true);
        formDialog.addThemeVariants(DialogVariant.LUMO_NO_PADDING);
        formDialog.getElement().setAttribute("aria-label", "Edit flashcard");
    }

    private Icon createIcon(VaadinIcon vaadinIcon, String label, String style) {
        Icon icon = vaadinIcon.create();
        icon.getStyle().set("padding", "4px");
        icon.getStyle().set("color", "var(--lumo-success-text-color");
        // Accessible label
        icon.getElement().setAttribute("aria-label", label);
        // Tooltip
        icon.getElement().setAttribute("title", label);
        icon.getElement().getThemeList().add(style);
        return icon;
    }

    private VerticalLayout createLayout(Dialog itemLayout) {
        var layout = new VerticalLayout();
        layout.setSpacing(false);
        layout.setPadding(false);
        layout.addClassName("flash-dialog");

        layout.setMargin(false);
        Section section = new Section();
        section.addClassNames("border-b", "border-contrast-10", "box-border", "flex", "h-xl", "items-end",
                "w-full");
        section.setWidthFull();
        H1 itemTitle = new H1(title);
        var horiz = new HorizontalLayout();
        horiz.setWidthFull();
        horiz.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        horiz.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        itemTitle.addClassName("flash-item-title");
        layout.addClassNames("flex", "flex-col", "items-start", "p-m", "rounded-l");

        var closeBtn = new Button(new Icon(VaadinIcon.CLOSE_SMALL));
        closeBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        closeBtn.addClickListener(event -> itemLayout.close());

        horiz.add(itemTitle, closeBtn);

        section.add(horiz);


        Paragraph itemDetail = new Paragraph(detail);
        itemDetail.addClassName("flash-item-detail");
        itemTitle.setWidthFull();
        itemDetail.addClassNames("text-xl");

        TextArea itemQuestion = new TextArea("Question");
        itemQuestion.setValue(question);
        itemQuestion.setWidthFull();
        itemQuestion.setReadOnly(true);

        var itemAnswerField = new TextArea("Enter Answer");
        itemAnswerField.setWidthFull();
        itemAnswerField.setRequired(true);

        layout.setWidthFull();

        itemLayout.setModal(false);
        itemLayout.setDraggable(false);
        itemLayout.addThemeVariants(DialogVariant.LUMO_NO_PADDING);
        itemLayout.setMaxHeight("85%");
        itemLayout.setMinWidth("36%");

        var submit = new Button("Submit");
        submit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        submit.addClassName("btn-dialog");

        submit.addClickListener(event -> checkedAnswer(itemLayout, itemAnswerField));

        var hl = new HorizontalLayout();
        hl.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        hl.setWidthFull();
        hl.add(submit);

        layout.add(section, itemDetail, itemQuestion, itemAnswerField, hl);

        return layout;
    }

    private void checkedAnswer(Dialog itemLayout, TextArea itemAnswerField) {
        if(answer.equals(itemAnswerField.getValue())){
            isCorrect = true;
            checked.setVisible(true);
            submitAnswer();
            itemLayout.close();
            Notification.show("You get the correct answer!", 5000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        } else {
            Notification.show("Wrong answer!", 5000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private void submitAnswer() {
        service.submitAnswer(id, isCorrect);
        formDialog.close();
        reload();
    }

    //TODO bind this @FlashCardView.class
    private VerticalLayout createDialogLayout(Dialog formDialog) {

        H3 dialogTitle = new H3("Edit flashcard");
        dialogTitle.addClassName("dialog-title");

        Header header = new Header(dialogTitle);
        header.addClassNames("border-b", "border-contrast-10", "box-border", "flex", "items-center", "w-full");
        header.setWidthFull();

        var createFields = createFlashField();

        var scrollContent = new VerticalLayout(createFields);

        var scroller = new Scroller(scrollContent);

        var footer = createFooter(formDialog);
        var dialogContent = new VerticalLayout();

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
        titleField.setValue(title);

        answerField.setWidthFull();
        answerField.setRequired(true);
        answerField.setValue(answer);

        questionField.setWidthFull();
        questionField.setRequired(true);
        questionField.setValue(question);

        descriptionField.setWidthFull();
        descriptionField.setLabel("Details");
        descriptionField.setRequired(true);
        descriptionField.setMaxLength(500);
        descriptionField.setValue(detail);
        descriptionField.setValueChangeMode(ValueChangeMode.EAGER);
        descriptionField.addValueChangeListener(e -> e.getSource().setHelperText(e.getValue().length() + "/" + 500));

        urlField.setWidthFull();
        urlField.setRequired(true);
        urlField.setLabel("Source");
        urlField.setValue(reference);
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

        save.addClassName("btn-dialog");

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        save.addClickListener(buttonClickEvent -> updateFlashCard());
        Button cancelButton = new Button("Cancel", e -> dialog.close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        HorizontalLayout buttonLayout = new HorizontalLayout(cancelButton,
                save);
        buttonLayout.setWidthFull();
        buttonLayout
                .setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        buttonLayout.addClassName("footer");

        return new Footer(buttonLayout);
    }

    private void updateFlashCard() {
        editFlashCard(id,
                titleField.getValue(),
                descriptionField.getValue(),
                urlField.getValue(),
                questionField.getValue(),
                answerField.getValue(),
                isCorrect);
    }

    private void editFlashCard(Integer id,
                               String title,
                               String detail,
                               String reference,
                               String question,
                               String answer,
                               boolean isCorrect) {
        try {
            service.update(id, title, detail, reference, question, answer, isCorrect);
            reload();
            formDialog.close();
        } catch (AuthException e) {
            Notification.show("You need to sign in as Guest or Create a new account",
                    5000,
                    Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_ERROR);
        }

    }

    public void reload(){
        UI.getCurrent().getPage().reload();
    }

    private MenuItem createIconItem(MenuBar menu, VaadinIcon iconName, String ariaLabel) {
        Icon icon = new Icon(iconName);
        icon.addClassName("flash-more-btn");
        MenuItem item = menu.addItem(icon);
        item.getElement().setAttribute("aria-label", ariaLabel);

        return item;
    }
}

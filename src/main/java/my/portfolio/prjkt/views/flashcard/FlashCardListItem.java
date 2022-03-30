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
import com.vaadin.flow.server.VaadinSession;
import my.portfolio.prjkt.data.entities.History;
import my.portfolio.prjkt.data.entities.MyUser;
import my.portfolio.prjkt.data.entities.Role;
import my.portfolio.prjkt.data.services.impl.DeviceServiceImp;
import my.portfolio.prjkt.data.services.impl.FlashCardServiceImp;
import my.portfolio.prjkt.exceptions.AuthException;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import java.util.List;

import static my.portfolio.prjkt.util.Utilities.createIconItem;

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
    private List<String> answerBy;
    private DeviceServiceImp device;
    private final FlashCardServiceImp service;

    Paragraph itemDetail;

    TextArea itemQuestion = new TextArea("Question");
    TextArea itemAnswerField = new TextArea("Enter Answer");


    HorizontalLayout horlayout = new HorizontalLayout();

    Icon checked;

    MyUser user = VaadinSession.getCurrent().getAttribute(MyUser.class);


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
                             List<String> answerBy,
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
        this.answerBy = answerBy;
        this.device = device;
        this.service = service;
        addClassNames("bg-contrast-5", "flex", "flex-col", "items-start", "p-m", "rounded-l");
        addClassName("material-list");
        this.checked = createIcon(VaadinIcon.CHECK, "", "");
        horlayout.addClassNames("flex", "items-start");
        horlayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        horlayout.setWidthFull();

        configureDialog();

        itemDetail = new Paragraph(detail);

        VerticalLayout zomLayout = createLayout(itemLayout);
        itemLayout.add(zomLayout);

        ComponentEventListener<ClickEvent<MenuItem>> listenerEdit = e -> formDialog.open();

        ComponentEventListener<ClickEvent<MenuItem>> listenerDelete = e -> {
            if(user != null && user.getRole().equals(Role.ADMIN)){
                deleteCard(id, service);
            } else {
                notificationUtils("You don't have permission to delete this card",
                        NotificationVariant.LUMO_ERROR);
            }
        };

        ComponentEventListener<ClickEvent<MenuItem>> listenerHistory = e -> {

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
        subMenu.addItem("Edit", listenerEdit);
        subMenu.addItem("Delete", listenerDelete);
        MenuItem menuAnswer = subMenu.addItem("Answered by");
        SubMenu userList = menuAnswer.getSubMenu();
        OrderedList orderedList = new OrderedList();
        userList.addItem("Users");
        for(String str : answerBy){
            ListItem listItem = new ListItem(new Span(str));
            listItem.setWidthFull();
            orderedList.add(listItem);
            userList.add(orderedList);
        }

        orderedList.addClassNames("list-none", "m-0", "p-0");

        subMenu.add(new Hr());
        MenuItem menuHistory = subMenu.addItem("History");
        SubMenu historyList = menuHistory.getSubMenu();
        historyList.addItem("History", listenerHistory);
        subMenu.addItem("Report");

        OrderedList historyOrderList = new OrderedList();

        List<String> listOfHistory = service.findAllHistory(id).stream().map(History::getActivityName).toList();

        for(String hisList : listOfHistory){
            ListItem listItem = new ListItem(new Span(hisList));
            listItem.setWidthFull();
            historyOrderList.add(listItem);
            historyList.add(historyOrderList);
        }
        historyOrderList.addClassNames("list-none", "m-0", "p-0");

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
        paragraph.setHeightFull();

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

    private void deleteCard(Integer id, FlashCardServiceImp service) {
        service.delete(id);
        reload();
    }

    private void configureDialog() {
        VerticalLayout dialogLayout = createDialogLayout(formDialog);
        formDialog.add(dialogLayout);
        formDialog.setModal(false);
        dialogLayout.addClassNames("max-w-screen-lg", "mx-auto");
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
        H1 itemTitle = new H1(title);
        itemTitle.addClassName("dialog-title");


        Header header = new Header(itemTitle);
        header.setWidthFull();
        header.addClassNames("border-b", "border-contrast-10", "box-border", "flex", "items-center", "w-full");
        itemTitle.addClassName("flash-item-title");
        layout.addClassNames("flex", "flex-col", "items-start", "p-m", "rounded-l");

        var closeBtn = new Button(new Icon(VaadinIcon.CLOSE_SMALL));
        closeBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        closeBtn.addClickListener(event -> itemLayout.close());
        itemTitle.setWidthFull();

        VerticalLayout createFields = createAnswerFields();
        VerticalLayout scrollContent = new VerticalLayout(createFields);
        Scroller scroller = new Scroller(scrollContent);
        scroller.setHeight("400px");

        Footer footer = createFooter2(itemLayout);
        footer.setHeight("70px");
        footer.setWidthFull();
        footer.addClassNames("bg-contrast-5", "flex", "items-center", "w-full");

        layout.setWidthFull();

        itemLayout.setModal(false);
        itemLayout.setDraggable(false);
        itemLayout.addThemeVariants(DialogVariant.LUMO_NO_PADDING);

        layout.add(header,scroller, footer);

        layout.setSpacing(false);
        layout.setPadding(false);
        layout.getStyle().remove("width");
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);
        layout.setClassName("dialog-no-padding-example-overlay");

        layout.getStyle().set("width", "380px").set("max-width", "100%");


        return layout;
    }

    private Footer createFooter2(Dialog dialog) {

        var submit = new Button("Submit");
        submit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        submit.addClassName("btn-dialog");

        submit.addClickListener(event -> checkedAnswer(itemLayout, itemAnswerField));
        Button cancelButton = new Button("Cancel", e -> dialog.close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        HorizontalLayout buttonLayout = new HorizontalLayout(cancelButton,
                submit);
        buttonLayout.setWidthFull();
        buttonLayout
                .setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        buttonLayout.addClassName("footer");

        return new Footer(buttonLayout);
    }

    private VerticalLayout createAnswerFields() {
        itemDetail.addClassName("flash-item-detail");
        itemDetail.addClassNames("text-m");

        itemQuestion.setValue(question);
        itemQuestion.setWidthFull();
        itemQuestion.setReadOnly(true);

        itemAnswerField.setWidthFull();
        itemAnswerField.setRequired(true);
        VerticalLayout section = new VerticalLayout(itemDetail,
                itemQuestion, itemAnswerField);

        section.setPadding(false);
        section.setSpacing(false);
        section.setAlignItems(FlexComponent.Alignment.STRETCH);
        section.getElement().setAttribute("role", "region");
        return section;
    }

    private void checkedAnswer(Dialog itemLayout, TextArea itemAnswerField)
            throws InvalidDataAccessApiUsageException {
        MyUser user = VaadinSession.getCurrent().getAttribute(MyUser.class);
        try{
            if(answer.equals(itemAnswerField.getValue()) && user != null){
                submitAnswer();
                itemLayout.close();
                notificationUtils("You get the correct answer!", NotificationVariant.LUMO_SUCCESS);
            } else if(user == null){
                notificationUtils("Failed to submit you need to sign-in first", NotificationVariant.LUMO_ERROR);
            } else {
                notificationUtils("Wrong answer!", NotificationVariant.LUMO_ERROR);
            }
        } catch (InvalidDataAccessApiUsageException e){
            notificationUtils("Correct and you already responds to this card", NotificationVariant.LUMO_SUCCESS);
            formDialog.close();
        }

    }

    private void submitAnswer(){
        try {
            isCorrect = true;
            checked.setVisible(true);
            service.submitAnswer(id, isCorrect);
            formDialog.close();
            reload();
        } catch (AuthException e){
            notificationUtils("Failed to submit you need to sign-in first", NotificationVariant.LUMO_ERROR);
        }

    }

    private void notificationUtils(String text, NotificationVariant lumoError) {
        Notification.show(text,
                5000,
                Notification.Position.TOP_CENTER).addThemeVariants(lumoError);
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
        dialogContent.getStyle().set("width", "380px").set("max-width", "100%");
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
            notificationUtils("You need to sign in as Guest or Create a new account", NotificationVariant.LUMO_ERROR);
        }

    }

    public void reload(){
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        UI.getCurrent().getPage().reload();
    }

}

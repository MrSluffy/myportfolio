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
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import my.portfolio.prjkt.data.services.impl.FlashCardServiceImp;

public class FlashCardListItem extends ListItem {

    Dialog formDialog = new Dialog();

    Dialog itemLayout = new Dialog();

    TextField titleField = new TextField();
    TextArea descriptionField = new TextArea();
    TextField urlField = new TextField();

    Button save = new Button("Save");
    private final Integer id;
    private final String title;
    private final String detail;
    private final String reference;
    private final String question;
    private final String answer;
    private final FlashCardServiceImp service;
    HorizontalLayout horlayout = new HorizontalLayout();

    Icon confirmed;

    public FlashCardListItem(Integer id, String title, String detail, String reference, String date, String question, String answer, FlashCardServiceImp service) {
        this.id = id;
        this.title = title;
        this.detail = detail;
        this.reference = reference;
        this.question = question;
        this.answer = answer;
        this.service = service;
        addClassNames("bg-contrast-5", "flex", "flex-col", "items-start", "p-m", "rounded-l");
        addClassName("material-list");
        this.confirmed = createIcon(VaadinIcon.CHECK, "", "");
        horlayout.addClassNames("flex", "items-start");
        horlayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        horlayout.setWidthFull();


        VerticalLayout dialogLayout = createDialogLayout(formDialog);
        dialogLayout.addClassNames("flex", "items-start", "p-l", "rounded-l");
        formDialog.add(dialogLayout);

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
        MenuBar menuBar = new MenuBar();
        menuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY_INLINE);
        menuBar.setOpenOnHover(true);
        MenuItem item = createIconItem(menuBar, VaadinIcon.ELLIPSIS_DOTS_V, "more");
        SubMenu subMenu = item.getSubMenu();
        subMenu.addItem("Edit", listener);
        subMenu.addItem("Delete", listenerDel);
        subMenu.add(new Hr());
        subMenu.addItem("Report");
        horlayout.add(header);


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
        bottomLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        bottomLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.END);
        bottomLayout.setWidthFull();
        bottomLayout.add(menuBar);


        add(horlayout, subtitle, anchorLayour, paragraph, bottomLayout);

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
        layout.setPadding(false);
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

        var submit = new Button("Submit");
        submit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        submit.addClassName("btn-dialog");

        submit.addClickListener(event ->{
            horlayout.add(confirmed);
            itemLayout.close();
        });

        var hl = new HorizontalLayout();
        hl.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        hl.setWidthFull();
        hl.add(submit);

        layout.add(section, itemDetail, itemQuestion, itemAnswerField, hl);

        return layout;
    }

    private VerticalLayout createDialogLayout(Dialog formDialog) {

        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(false);
        layout.setMargin(false);

        Section section = new Section();
        section.addClassNames("border-b", "border-contrast-10", "box-border", "flex", "h-xl", "items-end",
                "w-full");
        section.setWidthFull();

        HorizontalLayout badges = new HorizontalLayout();
        badges.getStyle().set("flex-wrap", "wrap");

        layout.addClassName("flash-dialog");

        H2 headline = new H2("Edit flashcard");
        headline.addClassName("flash-item-title");

        layout.addClassNames("flex", "flex-col", "items-start", "p-m", "rounded-l");


        var close = new Button(new Icon(VaadinIcon.CLOSE_SMALL));

        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        close.addClickListener(event -> formDialog.close());

        HorizontalLayout header = new HorizontalLayout(headline, close);
        header.setWidthFull();
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        section.add(header);

        layout.setWidthFull();

        formDialog.setModal(false);
        formDialog.setDraggable(true);
        formDialog.addThemeVariants(DialogVariant.LUMO_NO_PADDING);
        formDialog.setMaxHeight("80%");
        formDialog.setMinWidth("39%");

        titleField.setWidthFull();
        titleField.setLabel("Title");
        titleField.setValue(title);

        descriptionField.setWidthFull();
        descriptionField.setLabel("Details");
        descriptionField.setValue(detail);
        descriptionField.setMaxLength(200);
        descriptionField.setValueChangeMode(ValueChangeMode.EAGER);
        descriptionField.addValueChangeListener(e -> e.getSource().setHelperText(e.getValue().length() + "/" + 500));

        urlField.setWidthFull();
        urlField.setLabel("Source");
        urlField.setValue(reference);
        urlField.setHelperText("https://www.github.com/source.. etc");


        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClassName("btn-dialog");

        save.addClickListener(buttonClickEvent -> {
            editFlashCard(id, titleField.getValue(), descriptionField.getValue(), urlField.getValue());
        });
        var hl = new HorizontalLayout();
        hl.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.END);
        hl.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        hl.setWidthFull();
        hl.add(save);


        layout.add(section, titleField, descriptionField, urlField, hl);

        return layout;
    }

    private void editFlashCard(Integer id, String title, String detail, String reference) {
        service.update(id, title, detail, reference);
        formDialog.close();
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

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

    TextField titleField = new TextField();
    TextArea descriptionField = new TextArea();
    TextField urlField = new TextField();

    Button save = new Button("Save");
    private final Integer id;
    private final String title;
    private final String detail;
    private final String reference;
    private final FlashCardServiceImp service;

    public FlashCardListItem(Integer id, String title, String detail, String reference, String date, FlashCardServiceImp service) {
        this.id = id;
        this.title = title;
        this.detail = detail;
        this.reference = reference;
        this.service = service;
        addClassNames("bg-contrast-5", "flex", "flex-col", "items-start", "p-m", "rounded-l");
        Div div = new Div();
        addClassName("material-list");
        div.addClassNames("bg-contrast", "flex items-center", "justify-center", "mb-m", "overflow-hidden",
                "rounded-m w-full");
        div.addClassName("material-card");

        var horlayout = new HorizontalLayout();
        horlayout.addClassNames("flex", "items-start");
        horlayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        horlayout.setWidthFull();

        VerticalLayout dialogLayout = createDialogLayout(formDialog);
        dialogLayout.addClassNames("flex", "items-start", "p-l", "rounded-l");
        formDialog.add(dialogLayout);

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
        horlayout.add(header, menuBar);


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


        add(horlayout, subtitle, anchorLayour, paragraph);

    }

    private VerticalLayout createDialogLayout(Dialog formDialog) {

        VerticalLayout layout = new VerticalLayout();

        HorizontalLayout badges = new HorizontalLayout();
        badges.getStyle().set("flex-wrap", "wrap");

        layout.addClassName("flash-dialog");

        H2 headline = new H2("Edit flashcard");

        headline.addClassName("headline");

        Button close = new Button(new Icon(VaadinIcon.CLOSE_SMALL));

        close.addClickListener(event -> formDialog.close());

        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        HorizontalLayout header = new HorizontalLayout(headline, close);
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        header.setWidthFull();
        header.addClassNames("border-b", "border-contrast-10", "flex items-center", "justify-center");

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


        layout.add(header, titleField, descriptionField, urlField, hl);

        return layout;
    }

    private void editFlashCard(Integer id, String title, String detail, String reference) {
        service.update(id, title, detail, reference);
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

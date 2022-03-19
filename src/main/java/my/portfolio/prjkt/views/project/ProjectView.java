package my.portfolio.prjkt.views.project;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.dialog.DialogVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import my.portfolio.prjkt.data.entities.Project;
import my.portfolio.prjkt.data.entities.TypePrjkt;
import my.portfolio.prjkt.data.services.impl.ProjectServiceImp;
import my.portfolio.prjkt.views.MainLayout;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@PageTitle("Project")
@Route(value = "project", layout = MainLayout.class)
public class ProjectView extends Main implements HasComponents, HasStyle {

    private OrderedList imageContainer;

    private final ProjectServiceImp serviceImp;

    private Upload upload;

    Dialog formDialog = new Dialog();

    Button save = new Button("Save");


    TextField titleField = new TextField();
    TextField urlDownload = new TextField();
    TextArea descriptionField = new TextArea();
    TextField urlField = new TextField();

    private byte[] imageBytes;

    Button btn;

    public ProjectView(ProjectServiceImp serviceImp) {
        this.serviceImp = serviceImp;
        constructUI();

        Icon icon = new Icon(VaadinIcon.PLUS);
        icon.addClassName("btn-icon");
        icon.addClassNames("size-l");

        btn = new Button(icon);

        initImageContainer();

        configureProject();

        VerticalLayout dialogLayout = createDialogLayout(formDialog);
        dialogLayout.addClassNames("flex", "items-start", "p-l", "rounded-l");
        formDialog.add(dialogLayout);

    }

    private void initImageContainer() {

        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        upload = new Upload(buffer);

        Button btnUpload = new Button("Upload icon");
        upload.setUploadButton(btnUpload);
        upload.setWidthFull();
        upload.setMaxFiles(1);
        upload.setAcceptedFileTypes("image/jpeg","image/jpg", "image/png", "image/gif");

        upload.addSucceededListener(event -> {
            String attachmentName = event.getFileName();
            try {
                // The image can be jpg png or gif, but we store it always as png file
                BufferedImage inputImage = ImageIO.read(buffer.getInputStream(attachmentName));
                ByteArrayOutputStream pngContent = new ByteArrayOutputStream();
                ImageIO.write(inputImage, "png", pngContent);
                imageBytes = pngContent.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        upload.addFileRejectedListener(event -> {
            String errorMessage = event.getErrorMessage();

            Notification notification = Notification.show(
                    errorMessage,
                    5000,
                    Notification.Position.MIDDLE
            );
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        });

    }

    private VerticalLayout createDialogLayout(Dialog formDialog) {

        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(false);
        layout.setMargin(false);

        HorizontalLayout badges = new HorizontalLayout();
        badges.getStyle().set("flex-wrap", "wrap");

        Section section = new Section();
        section.addClassNames("border-b", "border-contrast-10", "box-border", "flex", "h-xl", "items-end",
                "w-full");
        section.setWidthFull();

        ComboBox<TypePrjkt> comboBox = new ComboBox<>("Category");
        comboBox.setWidthFull();
        comboBox.setItems(serviceImp.getAllProjectType());
        comboBox.setItemLabelGenerator(TypePrjkt::getName);
        comboBox.addValueChangeListener(e -> {
            Span filterBadge = createFilterBadge(e.getValue().getName());
            badges.add(filterBadge);
        });

        layout.addClassName("db-dialog");

        var headline = new H3("Add new project");

        headline.addClassName("headline");

        Button close = new Button(new Icon(VaadinIcon.CLOSE_SMALL));

        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        close.addClickListener(event -> formDialog.close());

        HorizontalLayout header = new HorizontalLayout(headline, close);
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        header.setWidthFull();
        header.getElement().getClassList().add("draggable");

        layout.setWidthFull();

        DatePicker.DatePickerI18n datePicker = new DatePicker.DatePickerI18n();


        datePicker.setDateFormat("yyyy-MM-dd");

        DatePicker singleFormatDatePicker = new DatePicker("Select a date:");
        singleFormatDatePicker.setPlaceholder(LocalDateTime.now().toLocalDate().toString());
        singleFormatDatePicker.setI18n(datePicker);
        singleFormatDatePicker.setWidthFull();

        if(singleFormatDatePicker.getValue() == null){
            singleFormatDatePicker.setValue(LocalDate.now());
        }


        formDialog.setModal(false);
        formDialog.setDraggable(true);
        formDialog.addThemeVariants(DialogVariant.LUMO_NO_PADDING);
        formDialog.setMaxHeight("80%");
        formDialog.setMinWidth("33%");

        titleField.setWidthFull();
        titleField.setLabel("Name");

        descriptionField.setWidthFull();
        descriptionField.setLabel("Description");
        descriptionField.setMaxLength(200);
        descriptionField.setValueChangeMode(ValueChangeMode.EAGER);
        descriptionField.addValueChangeListener(e -> e.getSource().setHelperText(e.getValue().length() + "/" + 200));

        urlField.setWidthFull();
        urlField.setLabel("Source code URL");
        urlField.setHelperText("https://www.github.com/source.. etc");

        urlDownload.setWidthFull();
        urlDownload.setLabel("Download URL");
        urlDownload.setHelperText("https://play.google.com/store/apps/details?id=... etc");


        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClassName("btn-dialog");

        save.addClickListener(buttonClickEvent -> {
            try {
                addNewPrjkt(
                        imageBytes,
                        titleField.getValue(),
                        comboBox.getValue(),
                        singleFormatDatePicker.getValue(),
                        descriptionField.getValue(),
                        urlField.getValue(), urlDownload.getValue());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        var hl = new HorizontalLayout();
        hl.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.END);
        hl.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        hl.setWidthFull();
        hl.add(save);

        section.add(header);


        layout.add(section, upload, titleField, comboBox, badges, descriptionField, singleFormatDatePicker, urlField, urlDownload, hl);

        return layout;
    }

    private void addNewPrjkt(byte[] imageBytes,
                             String title,
                             TypePrjkt typePrjkt,
                             LocalDate date,
                             String description,
                             String url,
                             String urlDownload) throws IOException {
        serviceImp.saveNewProject(
                imageBytes,
                title,
                typePrjkt,
                date,
                description,
                url,
                urlDownload);
        formDialog.close();
        imageContainer.removeAll();
        configureProject();
    }


    private Span createFilterBadge(String prjktType) {
        Button clearButton = new Button(VaadinIcon.CLOSE_SMALL.create());
        clearButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_TERTIARY_INLINE);
        clearButton.getStyle().set("margin-inline-start", "var(--lumo-space-xs)");
        // Accessible button name
        clearButton.getElement().setAttribute("aria-label", "Clear filter: " + prjktType);
        // Tooltip
        clearButton.getElement().setAttribute("title", "Clear filter: " + prjktType);

        Span badge = new Span(new Span(prjktType), clearButton);
        badge.getElement().getThemeList().add("badge contrast pill");

        // Add handler for removing the badge
        clearButton.addClickListener(event -> {
            badge.getElement().removeFromParent();
        });

        return badge;
    }

    private void configureProject() {
        for(Project prjkt : serviceImp.getAllProject()){
            imageContainer.add(new MaterialCardView(prjkt.getTitlePrjkt(), prjkt.getDescriptionPrjkt(),
                    serviceImp.generateImage(prjkt),prjkt.getUrlPrjkt(), prjkt.getTypePrjkt().getName(), prjkt.getDate(), prjkt.getUrlDownloadPrjkt()));
        }

        btn.addClassNames("bg-contrast-5", "flex", "flex-col", "items-start", "p-m", "rounded-l");
        btn.setHeightFull();
        btn.addClickListener(event->formDialog.open());
        btn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ICON);
        imageContainer.add(btn);
    }

    private void constructUI() {
        addClassNames("image-list-view", "max-w-screen-lg", "mx-auto", "pb-l", "px-l");

        HorizontalLayout container = new HorizontalLayout();
        container.addClassNames("items-center", "justify-between");

        VerticalLayout headerContainer = new VerticalLayout();
        H2 header = new H2("My Project");
        header.addClassName("header-h2");
        header.addClassNames("mb-0", "mt-xl", "text-3xl");
        headerContainer.add(header);

        imageContainer = new OrderedList();
        imageContainer.addClassNames("gap-m", "grid", "list-none", "m-0", "p-0");
        container.add(header);
        add(container, imageContainer);

    }
}
package my.portfolio.prjkt.views.project;

import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class MaterialCardView extends ListItem {

    public MaterialCardView(String title, String description, Image image, String url, String tag, String date, String urlDownload) {
        addClassNames("bg-contrast-5", "flex", "flex-col", "items-start", "p-m", "rounded-l");
        Div div = new Div();
        addClassName("material-list");
        div.addClassNames("bg-contrast", "flex items-center", "justify-center", "mb-m", "overflow-hidden",
                "rounded-m w-full");
        div.setHeight("160px");
        div.addClassName("material-card");

        image.addClassName("card-image");
        image.setWidth("100%");

        image.setAlt(title);

        div.add(image);

        Span header = new Span();
        header.addClassNames("text-xl", "font-semibold");
        header.setText(title);

        Span subtitle = new Span();

        var anchorLayour = new HorizontalLayout();
        anchorLayour.addClassNames( "flex", "wrap", "items-start");
        anchorLayour.setWidthFull();
        anchorLayour.setMargin(false);
        anchorLayour.setPadding(false);


        Anchor anchor = new Anchor("", "Source code");
        anchor.addClassNames("text-s");
        anchor.setHref(url);

        Anchor anchorDownload = new Anchor("", "Download");
        anchorDownload.addClassNames("text-s");
        anchorDownload.setHref(urlDownload);


        anchorLayour.add(anchor, anchorDownload);


        subtitle.addClassNames("text-s", "text-secondary");
        subtitle.setText(date);

        Paragraph paragraph = new Paragraph(description);
        paragraph.addClassName("my-m");

        Span badge = new Span();
        badge.getElement().setAttribute("theme", "badge");
        badge.setText(tag);

        addClickListener(listItemClickEvent -> {
//            getUI().ifPresent(ui -> ui.getPage().open(url));
        });

        add(div, header, subtitle, anchorLayour, paragraph, badge);

    }
}

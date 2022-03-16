package my.portfolio.prjkt.views.project;

import com.vaadin.flow.component.html.*;

public class MaterialCardView extends ListItem {

    public MaterialCardView(String title, String description, Image image, String url, String tag, String date) {
        addClassNames("bg-contrast-5", "flex", "flex-col", "items-start", "p-m", "rounded-l");

        Div div = new Div();
        div.addClassNames("bg-contrast", "flex items-center", "justify-center", "mb-m", "overflow-hidden",
                "rounded-m w-full");
        div.setHeight("160px");

        image.setWidth("100%");

        image.setAlt(title);

        div.add(image);

        Span header = new Span();
        header.addClassNames("text-xl", "font-semibold");
        header.setText(title);

        Span subtitle = new Span();
        subtitle.addClassNames("text-s", "text-secondary");
        subtitle.setText(date);

        Paragraph paragraph = new Paragraph(description);
        paragraph.addClassName("my-m");

        Span badge = new Span();
        badge.getElement().setAttribute("theme", "badge");
        badge.setText(tag);

        addClickListener(listItemClickEvent -> {
            getUI().ifPresent(ui -> ui.getPage().open(url));
        });

        add(div, header, subtitle, paragraph, badge);

    }
}

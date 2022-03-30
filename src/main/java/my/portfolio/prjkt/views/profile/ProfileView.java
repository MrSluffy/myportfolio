package my.portfolio.prjkt.views.profile;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import my.portfolio.prjkt.views.MainLayout;

@PageTitle("Profile")
@Route(value = "profile", layout = MainLayout.class)
public class ProfileView extends VerticalLayout {
    H1 name = new H1("John Andrew Camu");
    H2 title = new H2("Java Developer");

    String iconSize = "42px";

    public ProfileView() {
        setSpacing(false);
        Image img = new Image("images/dp-circle.png", "MrSluffy");
        img.addClassName("dp-profile");
        img.setWidth("300px");
        add(img);
        name.addClassName("name-profile");
        title.addClassName("title-profile");

        var btnResume = new Button("Download CV");
        btnResume.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnResume.addClassName("btn-profile");
        btnResume.addClickListener( e ->{
            openNewTab("https://drive.google.com/file/d/1mnsCN9j1_TxenizqvteohUi6lZRQ4Tgt/view?usp=sharing");
        });

        Paragraph paragraph = new Paragraph("Hi there, I'm a developer. " +
                "I loved to discover new features. For me features is everything," +
                " we are surrounded by the features. All we have to do is discover it." +
                " If I discover a new feature I'm not gonna say I create it. I discover it." +
                " Anyway's if you want to learn more about myself you can download my cv it's free :).");

        paragraph.addClassNames("text-2xl");
        paragraph.addClassName("paragraph-profile");
        paragraph.getStyle().set("width", "700px").set("max-width", "100%");

        add(name, title, getSocialLink(), btnResume, paragraph);
        addClassName("profile");

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

    private Component getSocialLink() {
        var socialLayout = new HorizontalLayout();
        socialLayout.addClassName("social-profile");
        String fbSrcPath = "icons/fb-media.png";
        String fbAlt = "Facebook Icon";
        Image fb = new Image(fbSrcPath, fbAlt);
        fb.addClassName("fb-profile");
        fb.setWidth(iconSize);

        fb.addClickListener( e -> {
            openNewTab("https://www.facebook.com/MrSluffyDev");
        });

        String ghSrcPath = "icons/gh-media.png";
        String ghAlt = "Github Icon";
        Image gh = new Image(ghSrcPath, ghAlt);
        gh.addClassName("gh-profile");
        gh.setWidth(iconSize);
        gh.addClickListener(e ->{
            openNewTab("https://github.com/MrSluffy");
        });

        String tgSrcPath = "icons/tg-media.png";
        String tgAlt = "Telegram Icon";
        Image tg = new Image(tgSrcPath, tgAlt);
        tg.addClassName("tg-profile");
        tg.setWidth(iconSize);
        tg.addClickListener(e ->{
           openNewTab("https://t.me/mrsluffy_releases");
        });

        String twitSrcPath = "icons/twit-media.png";
        String twitAlt = "Twitter Icon";
        Image twit = new Image(twitSrcPath, twitAlt);
        twit.setWidth(iconSize);
        twit.addClassName("twit-profile");
        twit.addClickListener( e ->{
           openNewTab("https://twitter.com/MrSluffy27");
        });

        socialLayout.add(fb, gh, tg, twit);

        return socialLayout;
    }

    public void openNewTab(String url){
        UI.getCurrent().getPage().open(url, "_blank");
    }

}

package my.portfolio.prjkt.views.home;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import de.mekaso.vaadin.addon.compani.AnimatedComponent;
import de.mekaso.vaadin.addon.compani.Animator;
import de.mekaso.vaadin.addon.compani.animation.AnimationBuilder;
import de.mekaso.vaadin.addon.compani.animation.AnimationTypes;
import de.mekaso.vaadin.addon.compani.effect.AttentionSeeker;
import de.mekaso.vaadin.addon.compani.effect.Delay;
import de.mekaso.vaadin.addon.compani.effect.Repeat;
import de.mekaso.vaadin.addon.compani.effect.Speed;
import my.portfolio.prjkt.views.MainLayout;

import java.util.Random;

@PageTitle("Home")
@Route(value = "home", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class HomeView extends VerticalLayout {


    HorizontalLayout layout = new HorizontalLayout();

    private final String[] mRandomGreetings = new String[]{"simple.", "unique.", "Andrew"};

    Animator animator = Animator.init(UI.getCurrent());


    Div labelDiv = new Div();

    public HomeView() {

        setSpacing(false);

        add(configureContent());

        setSizeFull();
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
    }

    private Component configureContent() {
        Image imgShape = new Image("images/image-shape.png", "business-shape-bg");
        imgShape.addClassName("image-shape");
        Image imgBusiness = new Image("images/image-business.png", "business");
        Label head = new Label("Hi, I'm " + getRandomGreetings());
        head.addClassName("h1-head");
        labelDiv.add(head);
        imgBusiness.addClassName("image-business");
        var text = new H4("I’m a developer with some unique ideas and knowledge in terms of technology.");
        text.addClassName("text-content");
        layout.add(labelDiv, text, imgBusiness, imgShape);
        AnimatedComponent animatedLabel = animator.prepareComponent(labelDiv);
        labelDiv.addClickListener(divClickEvent -> animatedLabel.animate(
                AnimationBuilder
                        .createBuilder()
                        .create(AnimationTypes.ComponentAnimation.class)
                        .withEffect(AttentionSeeker.pulse)
                        .withSpeed(Speed.normal)
                        .withDelay(Delay.noDelay)
                        .repeat(Repeat.Infinite)
        ));

        return layout;
    }

    private String getRandomGreetings() {
        return (this.mRandomGreetings[getLuckyNumber(0, 3) % mRandomGreetings.length]);
    }

    public int getLuckyNumber(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

}

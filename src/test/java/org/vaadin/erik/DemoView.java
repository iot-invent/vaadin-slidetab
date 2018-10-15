package org.vaadin.erik;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("")
@Push
public class DemoView extends Div {

    public DemoView() {
        getStyle().set("display", "flex");
        getStyle().set("flex-flow", "column");
        getStyle().set("padding", "60px");
        getStyle().set("box-sizing", "border-box");
        getStyle().set("align-items", "flex-start");

        Div content = new Div();
        content.setText("This is a test of our slide tab!");
        content.setHeight("500px");
        content.setWidth("300px");

        Div content2 = new Div();
        content2.setText("This is a test of our slide tab!");
        content2.setHeight("500px");
        content2.setWidth("300px");

        Div content3 = new Div();
        content3.setText("This is a test of our slide tab!");
        content3.setHeight("500px");
        content3.setWidth("300px");

        Div content4 = new Div();
        content4.setText("This is a test of our slide tab!");
        content4.setHeight("500px");
        content4.setWidth("300px");

        SlideTab topSlideTab = new SlideTabBuilder(content, "Click to expand this panel :)))))))))))))")
                .mode(SlideMode.TOP).tabPosition(SlideTabPosition.END).autoCollapseSlider(true).build();

        TextField durationField = new TextField("Animation duration");
        durationField.setValue("1000");
        durationField.setPattern("[0-9]+");
        Checkbox expandCheckbox = new Checkbox("Expand");
        Checkbox animatedCheckbox = new Checkbox("Animated");

        Button scheduleOptions = new Button("Schedule by options");
        scheduleOptions.addClickListener(event -> topSlideTab.scheduleExpand(
                expandCheckbox.getValue(), animatedCheckbox.getValue(), Integer.valueOf(durationField.getValue())));
        Button scheduleExpand = new Button("Schedule expand");
        scheduleExpand.addClickListener(event -> topSlideTab.scheduleExpand(Integer.valueOf(durationField.getValue())));
        Button scheduleCollapse = new Button("Schedule collapse");
        scheduleCollapse.addClickListener(event -> topSlideTab.scheduleCollapse(Integer.valueOf(durationField.getValue())));
        Button scheduleToggle = new Button("Schedule toggle");
        scheduleToggle.addClickListener(event -> topSlideTab.scheduleToggle(Integer.valueOf(durationField.getValue())));

        topSlideTab.addToggleListener(event -> {
            Notification.show("Panel " + (event.isExpand() ? "expanded!" : "collapsed!"), 2000, Notification.Position.TOP_CENTER);
        });

        add(topSlideTab);
        add(new SlideTabBuilder(content2, "Click to expand the other panel right here")
                .mode(SlideMode.RIGHT).tabPosition(SlideTabPosition.END).autoCollapseSlider(true).build());
        add(new SlideTabBuilder(content3, "Click to expand the other panel right here")
                .mode(SlideMode.BOTTOM).tabPosition(SlideTabPosition.END).autoCollapseSlider(true).build());
        add(new SlideTabBuilder(content4, "Click to expand the other panel right here")
                .mode(SlideMode.LEFT).tabPosition(SlideTabPosition.END).autoCollapseSlider(true).build());
        add(durationField, expandCheckbox, animatedCheckbox, scheduleOptions, scheduleExpand, scheduleCollapse, scheduleToggle);
    }
}

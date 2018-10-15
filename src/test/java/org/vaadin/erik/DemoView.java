package org.vaadin.erik;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

@Route("")
public class DemoView extends Div {

    public DemoView() {
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

        add(new SlideTabBuilder(content, "Click to expand this panel :)))))))))))))").mode(SlideMode.TOP).tabPosition(SlideTabPosition.END).build());
        add(new SlideTabBuilder(content2, "Click to expand the other panel right here").mode(SlideMode.RIGHT).tabPosition(SlideTabPosition.END).build());
        add(new SlideTabBuilder(content3, "Click to expand the other panel right here").mode(SlideMode.BOTTOM).tabPosition(SlideTabPosition.END).build());
        add(new SlideTabBuilder(content4, "Click to expand the other panel right here").mode(SlideMode.LEFT).tabPosition(SlideTabPosition.END).build());
    }
}

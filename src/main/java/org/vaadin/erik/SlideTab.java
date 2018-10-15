package org.vaadin.erik;

import java.util.Arrays;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;

@Tag("slide-tab")
@HtmlImport("src/slide-tab.html")
public class SlideTab extends PolymerTemplate<SlideTab.SlideTabModel> implements HasComponents, HasSize, HasStyle {

    @Id("tab")
    private Div tabComponent;
    @Id("content")
    private Div contentComponent;

    private SlideMode slideMode;
    private boolean expanded;
    private int pixelSize;
    private int animationDuration;
    private int zIndex;

    public SlideTab(SlideTabBuilder builder) {
        add(builder.content);

        slideMode = builder.mode;
        tabComponent.setHeight(builder.tabSize + "px");
        addClassName(builder.mode.toString().toLowerCase());

        setAnimationDuration(builder.animationDuration);
        setFixedContentSize(builder.pixel);
        setZIndex(builder.zIndex);
        setCaption(builder.caption);
        setTabPosition(builder.tabPosition);

        // TODO: getModel().setAutoCollapseSlider(builder.autoCollapseSlider);

        /*
        getState().mode = builder.mode;
        getState().tabPosition = builder.tabPosition;
        */

//        if (builder.listeners != null) {
//            builder.listeners.forEach(l -> {
//                addToggleListener(l);
//            });
//        }
        if (builder.styles != null) {
            for (String style : builder.styles) {
                addClassName(style);
            }
        }
    }

    public void expand() {
        expanded = true;
        getElement().callFunction("expand", pixelSize, slideMode.isVertical());
    }

    public void collapse() {
        expanded = false;
        getElement().callFunction("collapse", slideMode.isVertical());
    }

    @EventHandler
    public void toggle() {
        if (isExpanded()) {
            collapse();
        } else {
            expand();
        }
    }

    /**
     * Caption of the tab escape HTML
     */
    public void setCaption(final String caption) {
        getModel().setCaption(caption);
    }

    public String getCaption() {
        return getModel().getCaption();
    }

    /**
     * controls the position of the tab-panel<br>
     *
     * @param tabPosition by default MIDDLE
     */
    public void setTabPosition(final SlideTabPosition tabPosition) {
        Arrays.stream(SlideTabPosition.values())
                .forEach(value -> setClassName(value.name().toLowerCase(), value == tabPosition));
    }

    /**
     * @return duration in milliseconds
     */
    public int getAnimationDuration() {
        return animationDuration;
    }

    /**
     * set the animation duration<br>
     * by default 500ms
     *
     * @param animationDuration in milliseconds
     */
    public void setAnimationDuration(final int animationDuration) {
        this.animationDuration = animationDuration;
        contentComponent.getStyle().set("transition", String.format("height %dms, width %dms", animationDuration, animationDuration));
    }

    public void setFixedContentSize(final int pixelHeight) {
        this.pixelSize = pixelHeight;
    }

    public int getFixedContentSize() {
        return pixelSize;
    }

    /**
     * by default the {@link SliderPanel} stays open when use clicks outside<br>
     * when you enable autoCollapse the slider closes in mode of expand when user clicks somewhere else
     *
     * @param autoCollapseSlider enable auto collapse in expand state
     */
//    public void setAutoCollapseSlider(boolean autoCollapseSlider) {
//        getState().autoCollapseSlider = autoCollapseSlider;
//    }

    /**
     * z-Index of navigator, content and wrapper<br>
     * you can specify for multiple sliders which lays above another
     *
     * @param zIndex default <b>9990</b>
     */
    public void setZIndex(int zIndex) {
        this.zIndex = zIndex;
        getStyle().set("z-index", String.valueOf(zIndex));
    }

    public int getZIndex() {
        return zIndex;
    }

    /**
     * change to value when not already set
     *
     * @param expanded    true means expand
     * @param animated should be animated or not
     */
    public void setExpanded(final boolean expanded, final boolean animated) {
        if (this.expanded != expanded) {
            setAnimationDuration(animated ? animationDuration : 0);

            if (expanded) {
                expand();
            } else {
                collapse();
            }
        }
    }

    /**
     * it look only on state - a possible queued change is not checked
     *
     * @return is expanded
     */
    public boolean isExpanded() {
        return expanded;
    }


    /**
     * schedule a state change of the slider on client site<br>
     * a recall within the schedule will cancel the previous one
     *
     * @param value       true means expand
     * @param animated    should be animated or not
     * @param delayMillis millis in future the task will happen
     */
//    public void scheduleExpand(final boolean value, final boolean animated, final int delayMillis) {
//        getRpcProxy(SliderPanelClientRpc.class).scheduleExpand(value, animated, delayMillis);
//    }

    /**
     * schedule a change from expand to collapse vice versa in future. will trigger a timer on client site that will change the slider state
     * <br>
     * a recall within the schedule will cancel the previous one
     *
     * @param delayMillis millis in future the task will happen
     */
//    public void scheduleToggle(final int delayMillis) {
//        getRpcProxy(SliderPanelClientRpc.class).scheduleExpand(!getState().expand, true, delayMillis);
//    }

    /**
     * schedule a collapse in future. will trigger a timer on client site that will collapse the slider<br>
     * a recall within the schedule will cancel the previous one
     *
     * @param delayMillis millis in future the task will happen
     */
//    public void scheduleCollapse(final int delayMillis) {
//        getRpcProxy(SliderPanelClientRpc.class).scheduleExpand(false, true, delayMillis);
//    }

    /**
     * schedule an expand in future. will trigger a timer on client site that will expand the slider<br>
     * a recall within the schedule will cancel the previous one
     *
     * @param delayMillis millis in future the task will happen
     */
//    public void scheduleExpand(final int delayMillis) {
//        getRpcProxy(SliderPanelClientRpc.class).scheduleExpand(true, true, delayMillis);
//    }


//    public Registration addToggleListener(SliderPanelToggleListener listener) {
//        return this.addListener(SliderPanelToggleEvent.class, listener, SliderPanelToggleListener.ELEMENT_TOGGLED_METHOD);
//    }

    /**
     * allow to disable changing toggle<br>
     *     content is not disabled
     */
//    public void setEnabledToggle(boolean enabled) {
//        getState().enableToggle = enabled;
//    }
//
//    public boolean isEnabledToggle() {
//        return getState().enableToggle;
//    }

    public interface SlideTabModel extends TemplateModel {
        void setCaption(String caption);
        String getCaption();


        void setFlowInContent(boolean flowInContent);
        void setTabSize(int tabSize);
        void setAnimationDuration(int animationDuration);
        void setPixel(int pixel);
        void setAutoCollapseSlider(boolean autoCollapseSlider);
        void setZIndex(int zIndex);
    }

    /*
    protected SlideMode mode = SlideMode.TOP;

    protected SlideTabPosition tabPosition = SlideTabPosition.BEGINNING;
    */
}

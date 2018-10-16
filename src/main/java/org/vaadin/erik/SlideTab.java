package org.vaadin.erik;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.server.Command;
import com.vaadin.flow.shared.Registration;
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
    private boolean autoCollapsing;
    private boolean toggleEnabled;

    private int pixelSize;
    private int animationDuration;
    private int zIndex;

    private Timer timer = new Timer();
    private TabTask currentTask;

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
        setAutoCollapsing(builder.autoCollapseSlider);
        setToggleEnabled(true);

        if (builder.listeners != null) {
            builder.listeners.forEach(this::addToggleListener);
        }

        if (builder.styles != null) {
            for (String style : builder.styles) {
                addClassName(style);
            }
        }
    }

    public void expand() {
        expand(false);
    }

    private void expand(boolean fromClient) {
        if (!expanded && toggleEnabled) {
            expanded = true;
            getElement().callFunction("expand", pixelSize, slideMode.isVertical());
            fireEvent(new SlideToggleEvent(this, fromClient, true));
        }
    }

    public void collapse() {
        collapse(false);
    }

    private void collapse(boolean fromClient) {
        if(expanded && toggleEnabled) {
            expanded = false;
            getElement().callFunction("collapse", slideMode.isVertical());
            fireEvent(new SlideToggleEvent(this, fromClient, false));
        }
    }

    @EventHandler
    public void toggle() {
        if (isExpanded()) {
            collapse(true);
        } else {
            expand(true);
        }
    }

    @ClientCallable
    public void onOutsideClicked() {
        if (autoCollapsing && expanded) {
            collapse(true);
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

    public void setAutoCollapsing(boolean autoCollapsing) {
        this.autoCollapsing = autoCollapsing;
    }

    public boolean isAutoCollapsing() {
        return autoCollapsing;
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
        if (this.animationDuration != animationDuration) {
            this.animationDuration = animationDuration;
            contentComponent.getStyle().set("transition", String.format("height %dms, width %dms", animationDuration, animationDuration));
        }
    }

    public void setFixedContentSize(final int pixelHeight) {
        this.pixelSize = pixelHeight;
    }

    public int getFixedContentSize() {
        return pixelSize;
    }

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
    public void scheduleExpand(final boolean value, final boolean animated, final int delayMillis) {
        if (currentTask != null) {
            currentTask.cancel();
        }
        currentTask = new TabTask(() -> setExpanded(value, animated));
        timer.schedule(currentTask, delayMillis);
    }

    /**
     * schedule a change from expand to collapse vice versa in future. will trigger a timer on client site that will change the slider state
     * <br>
     * a recall within the schedule will cancel the previous one
     *
     * @param delayMillis millis in future the task will happen
     */
    public void scheduleToggle(final int delayMillis) {
        if (currentTask != null) {
            currentTask.cancel();
        }
        currentTask = new TabTask(this::toggle);
        timer.schedule(currentTask, delayMillis);
    }

    /**
     * schedule a collapse in future. will trigger a timer on client site that will collapse the slider<br>
     * a recall within the schedule will cancel the previous one
     *
     * @param delayMillis millis in future the task will happen
     */
    public void scheduleCollapse(final int delayMillis) {
        if (currentTask != null) {
            currentTask.cancel();
        }
        currentTask = new TabTask(this::collapse);
        timer.schedule(currentTask, delayMillis);
    }

    /**
     * schedule an expand in future. will trigger a timer on client site that will expand the slider<br>
     * a recall within the schedule will cancel the previous one
     *
     * @param delayMillis millis in future the task will happen
     */
    public void scheduleExpand(final int delayMillis) {
        if (currentTask != null) {
            currentTask.cancel();
        }
        currentTask = new TabTask(this::expand);
        timer.schedule(currentTask, delayMillis);
    }

    public Registration addToggleListener(ComponentEventListener<SlideToggleEvent> listener) {
        return this.addListener(SlideToggleEvent.class, listener);
    }

    /**
     * allow to disable changing toggle<br>
     *     content is not disabled
     */
    public void setToggleEnabled(boolean enabled) {
        this.toggleEnabled = enabled;
    }

    public boolean isToggleEnabled() {
        return toggleEnabled;
    }

    private class TabTask extends TimerTask {

        private Command command;

        private TabTask(Command command) {
            this.command = command;
        }

        @Override
        public void run() {
            getUI().ifPresent(ui -> ui.access(command));
        }
    }

    public interface SlideTabModel extends TemplateModel {
        void setCaption(String caption);
        String getCaption();
    }
}

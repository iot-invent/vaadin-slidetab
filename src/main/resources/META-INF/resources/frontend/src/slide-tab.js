import {PolymerElement, html} from '@polymer/polymer';
import { ThemableMixin } from '@vaadin/vaadin-themable-mixin/vaadin-themable-mixin.js';
import '@vaadin/vaadin-icons/vaadin-icons';

class SlideTab extends ThemableMixin(PolymerElement) {
    static get is() {
        return 'slide-tab'
    }

    static get template() {
        return html`
        <style>
            :host {
                --slide-tab-background-color: var(--lumo-primary-color, blue);
                --slide-tab-color: white;

                display: flex;
                align-items: center;
                box-sizing: border-box;
                position: absolute;
                color: var(--slide-tab-color);
                top: 0;
                left: 0;
                right: 0;
                bottom: 0;
            }
            
            :host {
                flex-flow: row;
                left: auto;
            }

           	:host #content {
                width: 0;
                margin-right: -500px;
            }
            
            :host #tab {
                border-radius: 5px 0 0 5px;
                right: 100%;
            }
            
            /* Styles for tab positioning END */
            :host #tab {
                top: 0;
            }
            

            #tab {
                position: absolute;
                display: flex;
                align-items: center;
                min-width: 120px;
                background-color: var(--slide-tab-background-color);
                white-space: nowrap;
                padding: 10px 5px;
            }
            #content {
                background-color: var(--slide-tab-background-color);
                align-self: stretch;
                position: relative;
                overflow: hidden;
            }
            #content ::slotted(*) {
                display: inline-block;
                padding: var(--lumo-space-m);
            }
            #tab::after, :host::after {
                content: "";
                position: absolute;
                top: 0; left: 0; bottom: 0; right: 0;
                z-index: -1;
                box-shadow: var(--lumo-box-shadow-m);
                border-radius: 5px 0 0 5px;
            }

            #expand, #collapse {
                padding-left: 1em;
                margin-left: auto;
            }
            #collapse, :host(.expanded) #expand {
                display: none;
            }
            :host(.expanded) #collapse, #expand {
                display: inline-block;
            }
        </style>

        <div part="tab" id="tab" on-click="toggle">
            [[caption]]
            <div id="expand">
                <slot name="expand">
                    <iron-icon icon="vaadin:vaadin:plus-circle"></iron-icon>
                </slot>
            </div>
            
            <div id="collapse">
                <slot name="collapse">
                    <iron-icon icon="vaadin:vaadin:minus-circle"></iron-icon>       
                </slot>
            </div>
        </div>
        <div part="content" id="content">
            <slot></slot>
        </div>`;
    }

    constructor() {
        super();
        this.outsideClickListener = this._onOutsideClick.bind(this);
    }

	rollOutFromRight(size) {
        let content = this.$.content;
        // Calculate the size if size is negative or zero
        if (size <= 0) {
            size = content.scrollWidth;
            size = Math.min(size, this._getMaxSize(false));
        }
        content.style.width = size + "px";
        content.style.marginRight = "0";

        this.classList.toggle("expanded", true);
        document.body.addEventListener("click", this.outsideClickListener);
    }
    
    expand(size, vertical) {
        let content = this.$.content;
        // Calculate the size if size is negative or zero
        if (size <= 0) {
            size = vertical ? content.scrollHeight : content.scrollWidth;
            size = Math.min(size, this._getMaxSize(vertical));
        }
        if (vertical) {
            content.style.height = size + "px";
        } else {
            content.style.width = size + "px";
        }

        this.classList.toggle("expanded", true);
        document.body.addEventListener("click", this.outsideClickListener);
    }

    rollInToRight() {
        
        this.$.content.style.marginRight = "-" + this.$.content.style.width;
        
        this.classList.toggle("expanded", false);
        document.body.removeEventListener("click", this.outsideClickListener);
    }
    
    collapse(vertical) {
        if (vertical) {
            this.$.content.style.height = "0";
        } else {
            this.$.content.style.width = "0";
        }

        this.classList.toggle("expanded", false);
        document.body.removeEventListener("click", this.outsideClickListener);
    }

    _onOutsideClick(event) {
        if (!this._isChildElement(event.target)) {
            this.$server.onOutsideClicked();
        }
    }

    _isChildElement(element) {
        while (element != null) {
            if (element == this) {
                return true;
            }
            element = element.parentNode;
        }
        return false;
    }

    /**
     * Returns the maximum size that the slide content can take, which is the width/height of the
     * body element minus the size of the tab.
     *
     * @param vertical      True if the slide opens in a vertical direction
     * @returns {number}    The maximum size
     * @private
     */
    _getMaxSize(vertical) {
        // Use the offsetHeight of the tab for both cases, as it's rotated for horizontal slides
        return vertical ? document.body.scrollHeight - this.$.tab.offsetHeight :
            document.body.scrollWidth - this.$.tab.offsetHeight;
    }


    connectedCallback() {
        super.connectedCallback();
        if (this.classList.contains("expanded")) {
            document.body.addEventListener("click", this.outsideClickListener);
        }
    }

    disconnectedCallback() {
        super.disconnectedCallback();
        document.body.removeEventListener("click", this.outsideClickListener);
    }
}

customElements.define(SlideTab.is, SlideTab);

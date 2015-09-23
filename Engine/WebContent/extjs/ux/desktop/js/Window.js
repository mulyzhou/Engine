/*
 * Ext JS Library 2.3.0
 * Copyright(c) 2006-2009, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */

Ext.ux.DeskWindow = function(config){
    Ext.apply(this, config);
    Ext.ux.DeskWindow.superclass.constructor.call(this);
};

Ext.extend(Ext.ux.DeskWindow, Ext.Window, {
	flyingRemove :function(){
		var c = this.items.get(0);
		if(c){
			this.items.remove(c);
            var dom = c.el.dom;
            dom.parentNode.removeChild(dom);
		}
	},
	
    close : function(){
        if(this.fireEvent("beforeclose", this) !== false){
            this.hide(null, function(){
                this.fireEvent('close', this);
                
                if(this.app){
                	this.app.desktop.removeWindow(this);
                }
                
                this.flyingRemove();
                
                this.destroy();
            }, this);
        }
    }
});
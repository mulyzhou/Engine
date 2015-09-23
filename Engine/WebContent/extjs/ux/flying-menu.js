Ext.namespace("Ext.flying");

Ext.flying.Menu = function(config){
	Ext.apply(this, config);
	
	Ext.flying.Menu.superclass.constructor.call(this);
};

Ext.extend(Ext.flying.Menu, Ext.menu.Menu, {
	
	// private
    render : function(){
        if(this.el){
            return;
        }
        var el = this.el = this.createEl();

        if(!this.keyNav){
            this.keyNav = new Ext.menu.MenuNav(this);
        }
        if(this.plain){
            el.addClass("x-menu-plain");
        }
        if(this.cls){
            el.addClass(this.cls);
        }
       	
		// generic focus element
        this.focusEl = el.createChild({
            tag: "div"
        });
			 
		this.grid.render(this.focusEl);
    }
});
/*
 * Ext JS Library 2.3.0
 * Copyright(c) 2006-2009, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */

Ext.apply(Ext, {
	fm : {
		msgCt : null,
		createBox : function(title, msg){
	        return ['<div class="msg">',
	                '<div class="x-box-tl"><div class="x-box-tr"><div class="x-box-tc"></div></div></div>',
	                '<div class="x-box-ml"><div class="x-box-mr"><div class="x-box-mc"><h3>', title, '</h3>', msg, '</div></div></div>',
	                '<div class="x-box-bl"><div class="x-box-br"><div class="x-box-bc"></div></div></div>',
	                '</div>'].join('');
	    },
	    msg : function(title, format){
	    	var me = this;
	        if(me.msgCt == null){
	            me.msgCt = Ext.DomHelper.insertFirst(document.body, {id:'msg-div'}, true);
	        }
	        
	        me.msgCt.alignTo(document, 'r-br',[0,-93]);
	        var s = String.format.apply(String, Array.prototype.slice.call(arguments, 1));
	        var m = Ext.DomHelper.append(me.msgCt, {html:me.createBox(title, s)}, true);
	        m.slideIn("b",{duration:0.5}).pause(10).ghost("b",{duration:5,remove:true});
	    }
	}
});
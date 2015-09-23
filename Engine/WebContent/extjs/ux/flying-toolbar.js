/**
 * 权限Toolbar标签
 * @author zjx 2011-10-25
 * @param security 按钮是否要进行权限验证
 * @param securityname 按钮权限验证的名称
 */
Ext.FlyingToolbar = Ext.extend(Ext.Toolbar, {
    initComponent : function() {
    	this.initFn();
    	Ext.FlyingToolbar.superclass.initComponent.call(this);
    },
    initFn:function(){
    	//根据权限设置按钮的显示和隐藏
    	var myitems = [];
    	Ext.each(this.items,function(item){
    		//未定义权限
    		if(item.securityname == undefined){
    			if(item == "->"){
    				myitems.push('->');
    			}else if(item instanceof Ext.BoxComponent){
    				myitems.push(item);
    			}else if(item["xtype"] == undefined){
    				myitems.push(new Ext.Button(item));
        			myitems.push('-');
    			}else {
    				myitems.push(item);
    			}
    		}else {
    		//是权限验证的按钮
        		var check = Ext.fcache.checkSecurity(item.securityname,1);
        		if(Ext.fcache.debug || check){	//如果权限通过验证，则显示按钮
        			if(item == "->"){
        				myitems.push('->');
        			}else if(item["xtype"] == undefined){
        				myitems.push(new Ext.Button(item));
            			myitems.push('-');
        			}else {
        				myitems.push(item);
        			}
        		}
        	}
    	});
    	
    	this.items = myitems;
    }
});

/**
 * 权限Tab标签
 * @author zjx 2011-10-26
 * @param security Panel是否要进行权限验证
 * @param securityname panel权限验证的名称
 */
Ext.FlyingTabPanel = Ext.extend(Ext.TabPanel, {
    state:'undo',	//记录状态 undo-无操作状态；add添加状态；edit修改状态
    panels:{},	//记录放进去的panel
	initComponent : function() {
    	Ext.FlyingTabPanel.superclass.initComponent.call(this);
    },
    getState:function(){	//获取Tab当前状态
    	return this.state;
    },
    setState:function(state){
    	if(state!='undo'&&state!='add'&&state!='edit'){
    		alert('设置了非法状态！');
    		return;
    	}
    	this.state = state;
    },
    insert:function(index,component){
    	component.security = component.security || false;
		component.btnsecurity = component.btnsecurity || false;//页面总是显示而权限控制在页面中的按钮
    	//权限过滤,按钮权限优先于页面权限
		var me = this;
		if(component.btnsecurity == true){//按钮权限	,例如编辑页面，权限控制在页面的按钮上
			//在component下查找btns
			var btns = component.buttons;
			Ext.each(btns,function(btn){
				if(btn.securityname != undefined){
					if(me.state == 'edit'){	//是编辑状态时才验证按钮的权限
						var check = Ext.fcache.checkSecurity(btn.securityname,1);
						if(Ext.fcache.debug || check){	//按钮权限
							btn.setVisible(true);
						}else{
							btn.setVisible(false);
						}
					}else{	//是添加状态或者无操作状态
						btn.setVisible(true);
					}
				}
			});
			this.panels[component.securityname]=component;
    		Ext.FlyingTabPanel.superclass.insert.call(this,index,component);
			return;
		}
		
    	if(component.securityname == undefined){	//页面权限
    		this.panels[component.securityname]=component;
    		Ext.FlyingTabPanel.superclass.insert.call(this,index,component);
    	}else{
    		var check = Ext.fcache.checkSecurity(component.securityname,3);
    		if(Ext.fcache.debug || check){	//通过权限验证
    			this.panels[component.securityname]=component;
    			Ext.FlyingTabPanel.superclass.insert.call(this,index,component);
    		}
    	}
    },
    remove:function(component,autoDestroy){
    	//权限过滤
    	if(component.securityname != undefined){
    		var check = Ext.fcache.checkSecurity(component.securityname,3);
    		if(Ext.fcache.debug || check){	//通过权限验证
    			this.panels[component.securityname]=null;
    			Ext.FlyingTabPanel.superclass.remove.call(this,component,autoDestroy);
    		}
    	}else{
    		this.panels[component.securityname]=component;
    		Ext.FlyingTabPanel.superclass.remove.call(this,component,autoDestroy);
    	}
    },
    setActiveTab:function(component){
    	if(typeof component == 'number'){	//初始化设置时，使用tab的索引
    		Ext.FlyingTabPanel.superclass.setActiveTab.call(this,component);
    		return;
    	}
    	//权限过滤
    	if(component.securityname != undefined){
    		var check = Ext.fcache.checkSecurity(component.securityname,3);
    		if(Ext.fcache.debug || check){	//通过权限验证
    			Ext.FlyingTabPanel.superclass.setActiveTab.call(this,component);
    		}
    	}else{
    		Ext.FlyingTabPanel.superclass.setActiveTab.call(this,component);
    	}
    }
});

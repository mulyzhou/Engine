function FlyingFaces(configs) {
	/** 全局属性区 */
	this.configs = configs || {};// 配置属性

	this.author = 'zdf';// 作者

	this.version = '1.2';// 版本

	this.listPanel = [];//多tabs容器
	
	/** 初始化操作区 */
	this.Constructor = function() {// 构造方法
		var me = this;

		for ( var name in me.configs) {// 初始化属性
			me[name] = me.configs[name];
		}

		me.handleTabs();// 处理按钮
	};
	
	this.handleTabs = function(){
		var me = this;
		
		me.tabs = me.tabs || new Array();
		
		for ( var m = 0; m < me.tabs.length; m++) {// 构建表格列	
			var src = me.tabs[m]["src"];
			var type = me.tabs[m]["type"] || "common";
			if(type == "email" || type == "mixface"){
				console.log("在混合模式下，不支持"+type+"风格！");
				type = "common";
			}
			var subTab = Ext.fcache.push(src,type);
			subTab.parent = me;
			if(subTab && subTab.center){
				me.listPanel.push(subTab.center);
			}
		}
	};
	
	this.Constructor();
	
	/** 容器 */
	this.center = new Ext.FlyingTabPanel({//tabs
    	resizeTabs : true,
    	autoDestroy : false,//关闭panel时，不销毁panel
    	activeTab : 0,
    	items:this.listPanel
    });
}
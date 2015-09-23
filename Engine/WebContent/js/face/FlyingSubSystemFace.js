function FlyingSubSystemFace(configs){
	/** 全局属性区 */
	this.configs = configs || {};// 配置属性
	
	this.cn = '';// 中文

	this.en = '';// 英文
	
	this.author = 'zdf';// 作者

	this.version = '1.0';// 版本
	
	this.rootId = '1';
	
	/** 初始化操作区 */
	this.Constructor = function() {// 构造方法
		var me = this;

		for ( var name in me.configs) {// 初始化属性
			me[name] = me.configs[name];
		}
	};
	this.Constructor();
	/** 资源树 */
	this.tree = new Ext.flying.Tree({
		region : "center",
		border : false,
		rootVisible : false, //是否显示根元素
		autoScroll : true,
		params : {"RESOURCE_ID" : this.rootId},
		url : 'common.action?command=T_SYS_USERRESOURCE.selectSubMenu',
		baseConfig : {idName : 'RESOURCE_ID',textName:'RESOURCE_NAME',pidName:'PID',iconName:'ICON'},
		rootNode : {RESOURCE_ID:this.rootId,RESOURCE_NAME:'根节点'},
		listeners : {
			'scope' : this,
			'click' : function(n) {
				var me = this;
				
					var data = n.attributes.data;
				if (data["FACETYPE"] != "subSystem") {
					var pageObj = Ext.fcache.get(data["RESOURCE_ADDR"],data["FACETYPE"],"",data["CACHE"]);
					//删除其他的panel
					me.tabs.flyingRemoveAll(false);
					//添加缓存中的panel
					me.tabs.add(pageObj["center"]);
					//激活panel
					me.tabs.layout.setActiveItem(pageObj["center"]);
					//页面刷新
					me.tabs.doLayout();
				}
			}
		}
	});
	
	this.tabs = new Ext.Panel({
    	region:'center',
    	border : false,
    	layout : 'card',
		activeItem : 0,
		margins : '2 2 2 0',
		border : false,
		items:[{
			id : 'start-panel',
			title : '欢迎使用',
			layout : 'fit',
			bodyStyle : 'padding:25px',
			html : '<img src="img/bg.jpg"/>'
		}]
    });
	
	this.center = new Ext.Panel({
	    layout : 'border',
	    title : this.cn,
	    items : [{
			title : '菜单',
			region : 'west',
			layout : 'border',
			margins : '2 0 2 2',
			width : 275,
			minSize : 200,
			maxSize : 350,
			collapsible : true,	//可以关闭
			split : true,
			items : this.tree
		}, {
			region : 'center',
			layout : 'border',
			border : false,
			items : [this.tabs]
		}]
	});
}

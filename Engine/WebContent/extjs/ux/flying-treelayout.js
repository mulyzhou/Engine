/**
 * 左侧树，右侧Panel的布局组件
 * @author zjx 2012-11-1
 */

Ext.flying.TreeLayout = Ext.extend(Ext.Panel,{
	tree:null,		//左侧树
	leftPanel:null, //左侧Panel
	rightPanel:null,//右侧panel
	treeClickFn:Ext.emptyFn,//点击树控件
	treebar:null,	//tree的上部工具条
	leftPanelTitle:'',	//左侧树的标题，只有设置标题才可以显示折叠按钮
	treeUrl:'',			//左侧树访问的url
	initComponent : function(config) {
		var me = this;
		
	    //1 构建树
	    var treeTemplate = {
	    	region : "center",
			border : false,
			rootVisible : false, //是否显示根元素
			autoScroll : true,
			loader : new Ext.flying.TreeLoader({
				dataUrl : me.treeUrl
			}),
			root : new Ext.tree.AsyncTreeNode({
				id : '0',
				iconCls : 'db-icn-world',
				text : '根目录'
			}),
			listeners : {
				'click' : me.treeClickFn
			}
	    };
	    
	    if(me.treebar){
	    	treeTemplate["tbar"] = me.treebar;
	    }
	    
		me.tree = new Ext.tree.TreePanel(treeTemplate);
		
		//3 构建左侧Panel
		var leftPanelTemplate = {
			region : 'west',
			layout : 'border',
			width : 200,
			minSize : 200,
			maxSize : 300,
			split : true, //分割开,可以拖动
			items : me.tree
		};
		
		//配置了显示标题
		if(me.leftPanelTitle){	
			leftPanelTemplate["title"] = me.leftPanelTitle;
			leftPanelTemplate["collapsible"] = true;
		}
		
		me.leftPanel = new Ext.Panel(leftPanelTemplate);
		
		//4 添加右侧Panel
		me.items.push(me.leftPanel);
		
		Ext.flying.TreeLayout.superclass.initComponent.call(this);
	}
});
	
Ext.reg('treeLayout',Ext.flying.TreeLayout);
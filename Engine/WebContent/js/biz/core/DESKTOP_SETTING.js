{ 
	"en" : "T_SYS_USERRESOURCE",
	"cn" : "菜单设置",
	"formColumn" : 1,
	"showFace" : function(val,last,row){
		var state = "";
		if(val == "permission"){
			state = "上下打开模式";
		}else if(val == "common"){
			state = "弹出打开模式";
		}else if(val == "email"){
			state = "右侧打开模式";
		}else if(val == "subSystem"){
			state = "导航打开模式";
		}else if(val == "empty"){
			state = "自定义打开模式";
		}
		
		return state;
	},
	"setting" : function(val,last,row){
		var red = "#FF6464",blue='blue',state;
		if(val == 1){
			state = "<span style='background-color:"+red+";width: 16px;height: 16px;vertical-align: middle;display: inline-block'></span><a href='javascript:void(0)' onclick='Ext.fcache.obj.startSetting("+row.data.RESOURCE_ID+",\""+row.data.RESOURCE_NAME+"\",\""+row.data.ICON+"\",\""+row.data.RESOURCE_ADDR+"\",\""+row.data.FACETYPE+"\","+row.data.CACHE+","+val+")'><span  style='color:"+red+"'>&nbsp;从桌面删除</span></a>";
		}else {
			state = "<span style='background-color:"+blue+";width: 16px;height: 16px;vertical-align: middle;display: inline-block'></span><a href='javascript:void(0)' onclick='Ext.fcache.obj.startSetting("+row.data.RESOURCE_ID+",\""+row.data.RESOURCE_NAME+"\",\""+row.data.ICON+"\",\""+row.data.RESOURCE_ADDR+"\",\""+row.data.FACETYPE+"\","+row.data.CACHE+","+val+")'><span  style='color:"+blue+"'>&nbsp;设置到桌面</span></a>";
		}
		return state;
	},
	"startSetting" : function(id,text,icon,src,type,cache,state){
		var me = this;
		Ext.Ajax.request({                           
	        url: 'common.action?command=T_SYS_USERRESOURCE.updateByUserIdResourceId',
	        method:'post',
	        params:{"RESOURCE_ID":id,"DESKTOP":!state},
	        success:function(response, opts){
	        	//刷新父页面
				me.ds.load();
				var data = {
						RESOURCE_ID : id,
						RESOURCE_NAME : text,
						ICON : icon,
						RESOURCE_ADDR : src,
						FACETYPE : type,
						CACHE : cache
				};
				
				if(state){
					Ext.fcache.App.removeShortcuts(data);
				}else{
					Ext.fcache.App.addShortcuts(data);
				};
	        },
	        failure: function(){
	        	Ext.Msg.show({
					title : '错误提示',
					msg : '初始化失败',
					buttons : Ext.Msg.OK,
					icon : Ext.Msg.ERROR
				});
	        }
	    });
	},
	"btns" : [ {
		"btype" : "refresh"
	},{
		"btype" : "modify",
		"sqlid" : "T_SYS_USERRESOURCE.updateByUserIdResourceId"
	}],
	"columns" : [ {
		"isPk" : true,
		"dataIndex" : "RESOURCE_ID",
		"header" : "菜单ID"
	}, {
		"isForm" : false,
		"dataIndex" : "RESOURCE_NAME",
		"isSearch": true,
		"width" : 250,
		"header" : "菜单名称"
	},{
		"dataIndex" : "ICON",
		"width" : 300,
		"header" : "显示图标"
	},{
		"isForm" : false,
		"dataIndex" : "RESOURCE_ADDR",
		"width" : 200,
		"hidden" : true,
		"header" : "地址"
	},{
		"isForm" : false,
		"width" : 200,
		"dataIndex" : "FACETYPE",
		"header" : "显示样式",
		"renderer" : 'showFace'
	},{
		"isColumn" : false,
		"xtype" : "combo",
		"dataIndex" : "FACETYPE",
		"header" : "显示样式",
		"displayField" : "name",
		"valueField" : "id",
		"data" : [ [ '上下打开模式','permission' ], [ '弹出打开模式','common' ],['右侧打开模式','email'],["导航打开模式",'subSystem'],["自定义打开模式",'empty'] ]
	},{
		"isForm" : false,
		"dataIndex" : "CACHE",
		"hidden" : true,
		"header" : "是否使用缓存"
	},{
		"isForm" : false,
		"hidden" : true,
		"header" : '设置',
		"width" : 100,
		"sortable" : false,
		"align" :'center',
		"dataIndex" : 'DESKTOP',
		"renderer" : 'setting'
	} ]
}
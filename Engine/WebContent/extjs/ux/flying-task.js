/*
 * Ext JS Library 2.3.0
 * Copyright(c) 2006-2009, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */

Ext.apply(Ext, {
	ft : {
		msgCt : null,
		showDetails : function(title,addr){
			var module = {
				"moduleTitle" : title,
				"moduleUrl" : addr
			};
				
			Ext.fcache.App.createWindow(module);
		},
	    show : function(title, format){
	    	var me = this;
	        if(me.msgCt == null){
	            me.msgCt = Ext.DomHelper.insertFirst(document.body, {id:'task-div'}, true);
	        }
	        
	        /** grid构建区域 */
	    	me.Model = Ext.data.Record.create([{
	    		name : 'REMIND_ID',type:'int'
	    	},{
	    		name : 'SYS_NAME',type:'string'
	    	},{
	    		name : 'LINK_ADDR',type:'string'
	    	},{
	    		name : 'USER_ID',type:'int'
	    	},{
	    		name : 'REMIND_SIZE',type:'int'
	    	}]);// 用户对象

	    	this.ds = new Ext.data.Store({// 数据源
	    		autoLoad : true,
	    		remoteSort : true,
	    		proxy : new Ext.data.HttpProxy({
	    			url : 'common.action?command=T_SYS_REMIND.selectAll'
	    		}),
	    		reader : new Ext.data.JsonReader({
	    			root : 'data',
	    			totalProperty : 'total'
	    		}, me.Model)
	    	});

	    	me.pagingBar = new Ext.PagingToolbar({// 分页工具栏
	    		pageSize : 10,
	    		store : me.ds,
	    		displayInfo : false
	    	});	    	
	    	
	    	me.showDetailsRender = function(val,last,row){
	    		var title = row.data.SYS_NAME;
	    		var addr = row.data.LINK_ADDR;
	    		return "<a href='javascript:void(0)' onclick='Ext.ft.showDetails(\""+title+"\",\""+addr+"\")'><span class='user-edit'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></a>";	
	    	};
	    	
	    	me.columns = [{
	    		"hidden" : true,
	    		"dataIndex" : "REMIND_ID",
	    		"header" : "提醒ID"
	    	},{
	    		"id":'SYS_NAME',
	    		"dataIndex" : "SYS_NAME",
	    		"header" : "系统简称"
	    	},{
	    		"dataIndex" : "REMIND_SIZE",
	    		"width" : 50,
	    		"align" :'center',
	    		"header" : "数量"
	    	},{
	    		"dataIndex" : "REMIND_ID",
	    		"header" : "",
	    		"width" : 60,
	    		"align" :'center',
	    		"renderer": me.showDetailsRender
	    	}];
	    	
	    	me.grid = new Ext.grid.FlyingGridPanel({// 列表
	    		title : '提醒',
	    		renderTo : 'task-div',
	    		iconCls : 'flyingTask',
	    		collapsible : true,
	    		autoExpandColumn: 'SYS_NAME',
	    		border : false,
	    		style : 'border-top:solid #99BBE8 1px;border-bottom:solid #99BBE8 2px',
	    		//loadMask : {
	    		//	msg : '数据加载中...'
	    		//},
	    		region : 'center',
	    		cm : me.columns,
	    		ds : me.ds,
	    		bbar : me.pagingBar,
	    		sm : new Ext.grid.CheckboxSelectionModel({
	    			singleSelect : true
	    		}),
	    		width : 270,
	    		autoHeight : true,
	    		enableColumnMove : false,
	    		trackMouseOver : false
	    	});
	                  
	        me.msgCt.alignTo(document, 'tr-tr').slideIn("tr",{duration:0.5});//.pause(10).ghost("r",{duration:5,remove:true});
	    }
	}
});
/*
 * Ext JS Library 2.3.0
 * Copyright(c) 2006-2009, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */

Ext.app.App = function(cfg){
    Ext.apply(this, cfg);
    this.addEvents({
        'ready' : true,
        'beforeunload' : true
    });

    Ext.onReady(this.initApp, this);
};

Ext.extend(Ext.app.App, Ext.util.Observable, {
    isReady: false,
    startMenu: null,
    modules: null,
    shortcuts:[],
    modules:[],
    taskRunner : new Ext.util.TaskRunner(),
    startConfig:{
    	title: 'zhengdifei',
        iconCls: 'user',
        toolItems: [{
        	text : '个人设置',
        	iconCls:'personal',
        	handler : function(){
        		Ext.fcache.App.personalSetting();
        	}
        },'-',{
        	text : '密码设置',
        	iconCls:'password',
        	handler : function(){
        		Ext.fcache.App.passwordSetting();
        	}
        },'-',{
            text:'菜单设置',
            iconCls:'settings',
            handler:function(){
            	Ext.fcache.App.desktopSetting();
            }
        },'-',{
        	text : '工具包',
        	iconCls:'tools',
        	handler : function(){
        		Ext.fcache.App.tools();
        	}
        },'-',{
            text:'退出系统',
            iconCls:'logout',
            handler:function(){
            	window.location = "login.html";
            }
        }
//        {
//        	text:'子系统导入',
//            iconCls:'up',
//            handler:function(){
//            	Ext.fcache.App.importSystem();
//            }
//        },'-',{
//        	text:'导出安装包',
//            iconCls:'down',
//            handler:function(){
//            	Ext.fcache.App.exportSystem();
//            }
//        }
        ]
    },
    initApp : function(){
    	this.init();

        this.desktop = new Ext.Desktop(this);
        
		this.startMenu = this.desktop.taskbar.startMenu;
		
		if(this.shortcuts){
			this.initShortcuts(this.shortcuts);
		}
		
        if(this.modules){
            this.initModules(this.modules);
        }
        
        Ext.EventManager.on(window, 'beforeunload', this.onUnload, this);
		this.fireEvent('ready', this);
        this.isReady = true;
        //获取登录信息
        this.setloginInfo();
        //初始化消息系统
        this.startGetMessage();
    },
    
    setloginInfo : function(){
		if(Ext.fcache.session != null && Ext.fcache.session["USER_NAME"] != null){
			this.startMenu.title = Ext.fcache.session["USER_NAME"];
		}else{
			this.startMenu.title = "未登录用户";
		}
    },
    
    init : function(){
    	var me = this;
    	
    	Ext.Ajax.request({
    		url : 'common.action',
    		async : false,
    		method : 'GET',
    		params : {'command':'T_SYS_USERRESOURCE.selectAll'},
    		success : function(response, opts) {
    			var obj = Ext.decode(response.responseText);
    			
    			me.modules = obj.data;
    			
    			me.shortcuts = [];
    			for(var i =0;i<obj.data.length;i++){
    				if(obj.data[i].DESKTOP == true){
    					me.shortcuts.push(obj.data[i]);
    				}
    			}
    		},
			failure : function(response, opts) {
				var obj = Ext.util.JSON.decode(response.responseText);
				Ext.Msg.show({
					title : '异常提示',
					msg : obj.msg,
					buttons : Ext.Msg.OK,
					icon : Ext.Msg.ERROR
				});
			}
    	});
    },
    
    initShortcuts : function(data){
    	while(Ext.get('x-desktop').child("dl")){
    		Ext.get('x-desktop').child("dl").remove();
    	};
    	
    	var tpl = new Ext.XTemplate(
    			'<dl>',
    		    '<tpl for=".">',
    		    	'<dt id="{RESOURCE_ID}*{RESOURCE_NAME}*{RESOURCE_ADDR}*{FACETYPE}*{CACHE}">',
    		    		'<a href="#"><img width="48px" height="48px" src="{ICON}" name="{RESOURCE_NAME}"/>',
    		    		'<div>{RESOURCE_NAME}</div></a>',
    		        '</dt>',
    		    '</tpl>',
    		    '</dl>'
    	);
    	
    	var subArr = [];
    	
    	for(var i = 0;i<data.length;i++){
    		subArr.push(data[i]);
    		
    		if((i+1)%7 == 0 || (i+1) == data.length){
    			tpl.append("x-desktop", subArr);
    			subArr = [];
    		}
    	}
    },
    
    removeShortcuts : function(data){
    	var me = this;
    	for(var i = 0;i<me.shortcuts.length;i++){
    		
		    if(me.shortcuts[i]["RESOURCE_ID"] == data.RESOURCE_ID){
		    	me.shortcuts.remove(me.shortcuts[i]);
		        break;
		    }
    	}
    	
    	me.initShortcuts(me.shortcuts);
    },
    
    addShortcuts : function(data){
    	var me = this;
    	
    	me.shortcuts.push(data);
    	
    	me.initShortcuts(me.shortcuts);
    },
    
    initModules : function(data){
		var tree = {}; 
		
		var root = {RESOURCE_ID:'72E79D997AE4441E90D4EB7842AE0F1D',TEXT:'根节点',children:[]};
		
		tree['id72E79D997AE4441E90D4EB7842AE0F1D'] = root;
		
		for(var i = 0;i<data.length;i++){
			data[i].children = [];
			tree['id'+data[i].RESOURCE_ID] = data[i];
		}
		
		for(var m = 0;m<data.length;m++){
			var obj = tree['id'+data[m].PID];
			if(obj){
				obj.children.push(data[m]);
			}
		}
		
		for(var n =0;n<tree['id72E79D997AE4441E90D4EB7842AE0F1D'].children.length;n++){
			var moduleData = tree['id72E79D997AE4441E90D4EB7842AE0F1D'].children[n];
			
			var menuTemplateObj = this.initMenu(moduleData);
			
	        this.startMenu.add(menuTemplateObj);
		}
    },
    
    initMenu : function(menu){
		var menuTemplateObj = {
				text : menu.RESOURCE_NAME,
				app : this,
				icon : menu.ICON,
				RESOURCE_ID : menu.RESOURCE_ID,
				moduleTitle : menu.RESOURCE_NAME,
				moduleUrl : menu.RESOURCE_ADDR,
				moduleType : menu.FACETYPE,
				moduleCache : menu.CACHE
		};
		
		if(menu.children.length > 0 ){
			if(menu.FACETYPE == 'subSystem'){
				menuTemplateObj.handler = this.menuCreateWindow;
			}else{
				menuTemplateObj.handler = function(){return false;};
			}
			
			var items = [];
			
			for(var i =0;i<menu.children.length;i++){
				items.push(this.initMenu(menu.children[i]));
			};
			
			menuTemplateObj.menu = {
					items:items
			};
		}else{
			menuTemplateObj.handler = this.menuCreateWindow;
		}
		
		return menuTemplateObj;
	},
	
	startGetMessage : function(){
		 var task_checkMessage = {
					scope : this,
					run: this.checkMessage,//执行任务时执行的函数
					interval: 10000//任务间隔，毫秒为单位，这里是10秒
		};
		
		if(Ext.fcache.receiveMessage){
			this.taskRunner.start(task_checkMessage);//初始化时就启动任务 
		}
	},
	
	stopGetMessage : function(){
		this.taskRunner.stopAll();//初始化时就启动任务 
	},
	
	checkMessage : function(){
		var me = this;
		//获取最新消息
		Ext.Ajax.request({
    		url : 'common.action',
    		method : 'GET',
    		params : {'command':'T_SYS_MESSAGE.getUserMessage'},
    		success : function(response, opts) {
    			var obj = Ext.decode(response.responseText);
    			
    			me.desktop.taskbar.msgMenu.removeAll();
    			
    			for(var i =0;i<obj.data.length;i++){
    				var msgObj = {};
    				var title = "【<B>"+obj.data[i].SYS_NAME+"</B>】" + 
    				obj.data[i].MSG_TITLE +
    				"("+obj.data[i].SEND_DATE+")";
    				
    				msgObj.text = title;
    				msgObj.iconCls = "message_new";
    				msgObj.app = me;
    				msgObj.moduleId = obj.data[i].MSG_ID;
    				msgObj.moduleTitle = obj.data[i].MSG_TITLE;
    				msgObj.moduleUrl = obj.data[i].MSG_ADDR;
    				msgObj.moduleParam = obj.data[i].MSG_ADDR_PARAM;
    				msgObj.handler = me.msgCreateWindow;
    				
    				me.desktop.taskbar.msgMenu.add(msgObj);
    			}
    			
    			me.desktop.taskbar.msgBtn.setText("("+obj.data.length+")");
    		},
			failure : function(response, opts) {
				var obj = Ext.util.JSON.decode(response.responseText);
				Ext.Msg.show({
					title : '异常提示',
					msg : obj.msg,
					buttons : Ext.Msg.OK,
					icon : Ext.Msg.ERROR
				});
			}
    	});
		
		//获取最新任务
		Ext.Ajax.request({
    		url : 'common.action',
    		method : 'GET',
    		params : {'command':'T_SYS_REMIND.getUserRemind'},
    		success : function(response, opts) {
    			var obj = Ext.decode(response.responseText);
    			if(obj.data.SUMSIZE > 0){
        			me.desktop.taskbar.taskBtn.setText("("+obj.data.SUMSIZE+")");
    			}
    			
    			me.desktop.taskbar.taskDs.reload();
    		},
			failure : function(response, opts) {
				var obj = Ext.util.JSON.decode(response.responseText);
				Ext.Msg.show({
					title : '异常提示',
					msg : obj.msg,
					buttons : Ext.Msg.OK,
					icon : Ext.Msg.ERROR
				});
			}
    	});
		
		
	},
	
    createWindow : function(module,width,height){
    	var me = this;
    	
    	var win = me.desktop.getWindow(module.moduleUrl);
    	
        if (!win) {
        	width = width || 830;
        	height = height || 550;
        	var title = module.moduleTitle;
        	var url = module.moduleUrl;
			var type = module.moduleType || "permission";
			var cache = module.moduleCache || 1;
			var page = null;
			
			if(type == "subSystem"){
				var configs = {
						cn : module.moduleTitle,
						rootId : module.RESOURCE_ID
				};
				
				page = new FlyingSubSystemFace(configs);
			}else{
				//从缓存中请求
				page = Ext.fcache.get(url,type,"",cache);
			}
        	
			
			if(page){
				
				if(page["beforeCreate"]){
					var result = page["beforeCreate"]();
					if(!result)return;
				}
				
	            win = me.desktop.createWindow({
	            	title: title,
	            	app: me,
	            	width: width,
	                height: height,
	                layout: 'fit',
	                items: page.center
	            },Ext.ux.DeskWindow);
	            
	            if(!(type == "email" || type == "mixface")){
		            page.center.setTitle(null);
	            }

	            page.center.setHeight();
	            
	            me.desktop.addWindow(url,win);
	            
	            win.show();
	            
				if(page["afterCreate"]){
					page["afterCreate"]();
				}
			}else{
				alert("error");
			}
        }else{
        	  win.show();
        }
    },
    
    menuCreateWindow: function(module) {
    	var me = this;

    	var win = me.app.desktop.getWindow(module.moduleUrl);
    	
        if (!win) {
        	
        	var width = module.width || 830;
        	var height = module.height || 550;
        	var title = module.moduleTitle;
        	var url = module.moduleUrl;
			var type = module.moduleType || "permission";
			var cache = module.moduleCache || 1;
			var page = null;
			
			if(type == "subSystem"){
				var configs = {
						cn : module.moduleTitle,
						rootId : module.RESOURCE_ID
				};
				
				page = new FlyingSubSystemFace(configs);
			}else{
				//从缓存中请求
				page = Ext.fcache.get(url,type,"",cache);
			}
			
			if(page){
				
				if(page["beforeCreate"]){
					var result = page["beforeCreate"]();
					if(!result)return;
				}
				
	            win = me.app.desktop.createWindow({
	            	title: title,
	            	app: me.app,
	            	width: width,
	                height: height,
	                layout: 'fit',
	                items: page.center
	            },Ext.ux.DeskWindow);
	            
	            if(type !== "email"){
		            page.center.setTitle(null);
	            }
	            page.center.setHeight();
	            
	            me.app.desktop.addWindow(url,win);
	            
	            win.show();	
	            
	            if(page["afterCreate"]){
					page["afterCreate"]();
				}
			}else{
				alert("error");
			}
			
        }else{
            win.show();	
        }
    },
    
    msgCreateWindow: function(module) {
    	var me = this;

    	var width = module.width || 780;
    	var height = module.height || 550;
    	var title = module.moduleTitle;
    	var url = module.moduleUrl;
		var type = module.moduleType || "common";
		var params = {};
		
		if(module.moduleParam){
			var paramObj = Ext.util.JSON.decode(module.moduleParam);
			var filter = "";
			for ( var name in paramObj) {// 初始化属性
				filter += name + "="+paramObj[name]+" AND ";
			}
			filter += "1=1";
			params["filter"] = filter;
		}
		//从缓存中请求
		var page = Ext.fcache.push(url,type,params);
		
		if(page){
			
			if(page["beforeCreate"]){
				var result = page["beforeCreate"]();
				if(!result)return;
			}
			
            win = me.app.desktop.createWindow({
            	title: title,
            	app: me.app,
            	width: width,
                height: height,
                layout: 'fit',
                items: page.center
            },Ext.ux.DeskWindow);
            
            if(type !== "email"){
	            page.center.setTitle(null);
            }
            page.center.setHeight();
            
            me.app.desktop.addWindow(url,win);
            
            win.show();	
            
            if(page["afterCreate"]){
				page["afterCreate"]();
			}
            //改成已读
            Ext.Ajax.request({
        		url : 'common.action',
        		method : 'GET',
        		params : {'command':'T_SYS_MESSAGE.readMessage',"MSG_ID":module.moduleId},
        		success : function(response, opts) {
        		},
				failure : function(response, opts) {
					var obj = Ext.util.JSON.decode(response.responseText);
					Ext.Msg.show({
						title : '异常提示',
						msg : obj.msg,
						buttons : Ext.Msg.OK,
						icon : Ext.Msg.ERROR
					});
				}
        	});
		}else{
			alert("error");
		}
    },
    
    personalSetting : function(){
    	var me = this;
    	
    	if(!me.personalWin){
    		me.personalSave = new Ext.Button({
        		text : '提交',
        		iconCls : 'save',
        		scope:me,
        		handler : function(btn) {
        			if (me.personalForm.getForm().isValid()) {
        				me.personalForm.getForm().submit({
        					url : "common.action?command=T_SYS_USERINFO.update",
        					params : {"USER_ID" : Ext.fcache.session["USER_ID"]},
        					waitTitle : '请稍候',
        					waitMsg : '正在提交表单数据,请稍候...',
        					success : function(form, action) {
        						if(!action.result.success){
        							Ext.Msg.show({
        								title : '异常提示',
        								msg : action.result.msg,
        								buttons : Ext.Msg.OK,
        								icon : Ext.Msg.ERROR
        							});
        							return;
        						};
        						
        						me.personalWin.hide();
        						Ext.fm.msg("个人信息","恭喜，保存成功！");
        					},
        					failure : function(form, action) {
        						Ext.Msg.show({
        							title : '错误提示',
        							msg : '网络或者服务器出现异常，无法连接!',
        							buttons : Ext.Msg.OK,
        							icon : Ext.Msg.ERROR
        						});
        					}
        				})
        			}
        		}
        	});
        	
        	me.personalForm = new Ext.FormPanel({
        		region : 'center',
        		labelAlign : 'right',
        		labelWidth : 70,
        		bodyStyle : 'padding:2px',
        		frame : true,
        		border : false,
        		autoScroll : true,
        		defaultType: 'textfield',
        		defaults : {
        			anchor : '93%'
        		},
        		items : [{
    				 fieldLabel: '姓名',
    	             name: 'USER_NAME',
    	             allowBlank:false,
    	             value : Ext.fcache.session["USER_NAME"]    	    
    			},{
    				 fieldLabel: '登录名',
    	             name: 'LOGIN_NAME',
    	             allowBlank:false,
    	             value : Ext.fcache.session["LOGIN_NAME"],
    	             validator:function(value){
     	         		if(Ext.fcache.session["LOGIN_NAME"] != value){
     	         			var result = true;
     	         			Ext.Ajax.request({                           
     	             			url : 'common.action?command=T_SYS_USERINFO.validateName',
     	         				params : {LOGIN_NAME:value},
     	         				async: false,
     	             	        method:'GET',
     	             	        success:function(response, opts){
     	             	        	var responseArray = Ext.util.JSON.decode(response.responseText);
     	             	        	if(responseArray.data > 0){
     	             	        		result = "所填账号已经存在！";
     	             	        	}
     	             	        }
     	             	    });
     	         			return result;
     	         		}else{
     	         			return true;
     	         		}
     	         	}
    			},{
	   				 fieldLabel: '所在部门',
		             name: 'DWMC',
		             disabled : true,
		             value : Ext.fcache.session["DWMC"]
    			}]
        	});
        	
        	me.personalWin = new Ext.Window({
    			title : '个人基本信息维护',
    			layout : 'border',
    			width : 500,
    			height : 300,
    			frame : true,
    			closeAction : 'hide',
    			items : me.personalForm,
    			buttonAlign : 'center',
    			buttons : [me.personalSave]
    		});
		}
    	
		me.personalWin.show();
    },
    
    passwordSetting : function(){
    	var me = this;
    	
    	if(!me.passwordWin){
    		me.passwordSave = new Ext.Button({
        		text : '提交',
        		iconCls : 'save',
        		scope:me,
        		handler : function(btn) {
        			if (me.passwordForm.getForm().isValid()) {
        				me.passwordForm.getForm().submit({
        					url : "common.action?command=T_SYS_USERINFO.modifyPassword",
        					params : {"USER_ID" : Ext.fcache.session["USER_ID"]},
        					waitTitle : '请稍候',
        					waitMsg : '正在提交表单数据,请稍候...',
        					success : function(form, action) {
        						console.log(action.result);
        						if(!action.result.success){
        							Ext.Msg.show({
        								title : '异常提示',
        								msg : action.result.msg,
        								buttons : Ext.Msg.OK,
        								icon : Ext.Msg.ERROR
        							});
        							
        							return;
        						};
        						
        						if(action.result.data){
        							me.passwordWin.hide();
        							Ext.fm.msg("密码修改","恭喜，保存成功！");
    							}else{
    								Ext.Msg.show({
            							title : '错误提示',
            							msg : '原密码输入错误',
            							buttons : Ext.Msg.OK,
            							icon : Ext.Msg.ERROR
            						});
    							}
        					},
        					failure : function(form, action) {
        						Ext.Msg.show({
        							title : '错误提示',
        							msg : '网络或者服务器出现异常，无法连接!',
        							buttons : Ext.Msg.OK,
        							icon : Ext.Msg.ERROR
        						});
        					}
        				})
        			}
        		}
        	});
        	
        	me.passwordForm = new Ext.FormPanel({
        		region : 'center',
        		labelAlign : 'right',
        		labelWidth : 70,
        		bodyStyle : 'padding:2px',
        		frame : true,
        		border : false,
        		autoScroll : true,
        		defaultType: 'textfield',
        		defaults : {
        			anchor : '93%'
        		},
        		items : [{
	    				 fieldLabel: '原密码',
	    	             name: 'OLD_PASSWORD',
	    	             inputType : 'password',
	    	             allowBlank:false 	    
	    			},{
	   				 fieldLabel: '新密码',
		             name: 'PASSWORD',
		             inputType : 'password',
		             allowBlank:false 	    
	    			},{
    				 fieldLabel: '新密码确认',
    	             name: 'NEW_PASSWORD',
    	             inputType : 'password',
    	             allowBlank:false,
    	             validator:function(value){
     	         		if(me.passwordForm.getForm().findField("PASSWORD").getValue() != value){
     	         			return "与新密码不符";
     	         		}else{
     	         			return true;
     	         		}
     	         	}
    			}]
        	});
        	
        	me.passwordWin = new Ext.Window({
    			title : '个人基本信息维护',
    			layout : 'border',
    			width : 500,
    			height : 300,
    			frame : true,
    			closeAction : 'hide',
    			items : me.passwordForm,
    			buttonAlign : 'center',
    			buttons : [me.passwordSave]
    		});
		}
    	
		me.passwordWin.show();
    },
    
    desktopSetting : function(){
    	var module = {};
    	module.moduleTitle = "菜单设置";
        module.moduleUrl = "biz/core/DESKTOP_SETTING.js";
        module.moduleType = "common";
        
        this.createWindow(module);
    },
    
    importSystem : function(){
    	var module = {};
    	module.moduleTitle = "子系统导入";
        module.moduleUrl = "face/import.js";
        module.moduleType = "empty";
        
        this.createWindow(module,450,150);
    },
    
    exportSystem : function(){
    	var exportForm = new Ext.FormPanel();
    	exportForm.applyToMarkup(Ext.DomHelper.append(Ext.getBody(), {
			tag : "div"
		}, true));
    	exportForm.getForm().getEl().dom.action = "export";  
    	exportForm.getForm().getEl().dom.submit();
    },
    
    tools : function(){
    	
    },
    
    getModule : function(name){
    	var ms = this.modules;
    	for(var i = 0, len = ms.length; i < len; i++){
    		if(ms[i].id == name || ms[i].appType == name){
    			return ms[i];
			}
        }
        return '';
    },

    onReady : function(fn, scope){
        if(!this.isReady){
            this.on('ready', fn, scope);
        }else{
            fn.call(scope, this);
        }
    },

    getDesktop : function(){
        return this.desktop;
    },

    onUnload : function(e){
        if(this.fireEvent('beforeunload', this) === false){
            e.stopEvent();
        }
    }
});
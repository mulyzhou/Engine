/*
 * Ext JS Library 2.3.0
 * Copyright(c) 2006-2009, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */

Ext.Desktop = function(app){
	this.taskbar = new Ext.ux.TaskBar(app);
	var taskbar = this.taskbar;
	
	var desktopEl = Ext.get('x-desktop');
    var taskbarEl = Ext.get('ux-taskbar');

    this.windows = {};
    var activeWindow;
    
    function minimizeWin(win){
        win.minimized = true;
        win.hide();
    }

    function markActive(win){
        if(activeWindow && activeWindow != win){
            markInactive(activeWindow);
        }
        taskbar.setActiveButton(win.taskButton);
        activeWindow = win;
        Ext.fly(win.taskButton.el).addClass('active-win');
        win.minimized = false;
    }

    function markInactive(win){
        if(win == activeWindow){
            activeWindow = null;
            Ext.fly(win.taskButton.el).removeClass('active-win');
        }
    }

    function removeWin(win){
    	taskbar.removeTaskButton(win.taskButton);
        layout();
    }

    function layout(){
        desktopEl.setHeight(Ext.lib.Dom.getViewHeight()-taskbarEl.getHeight());
    }
    Ext.EventManager.onWindowResize(layout);

    this.layout = layout;

    this.createWindow = function(config, cls){
    	var win = new (cls||Ext.Window)(
            Ext.applyIf(config||{}, {
                minimizable: true,
                maximizable: true
            })
        );
        win.render(desktopEl);
        win.taskButton = taskbar.addTaskButton(win);

        win.cmenu = new Ext.menu.Menu({
            items: [

            ]
        });

        win.animateTarget = win.taskButton.el;
        
        win.on({
        	'activate': {
        		fn: markActive
        	},
        	'beforeshow': {
        		fn: markActive
        	},
        	'deactivate': {
        		fn: markInactive
        	},
        	'minimize': {
        		fn: minimizeWin
        	},
        	'close': {
        		fn: removeWin
        	}
        });
        
        layout();
        return win;
    };

    this.getManager = function(){
        return this.windows;
    };
    
    this.addWindow = function(name,win){
    	this.windows[name] = win;
    };
    
    this.removeWindow = function(win){
    	for(var name in this.windows){
    		if(this.windows[name] == win){
    			delete this.windows[name];
    		}
    	}
    };
    
    this.getWindow = function(name){
        return this.windows[name];
    };
    
    this.getWinWidth = function(){
		var width = Ext.lib.Dom.getViewWidth();
		return width < 200 ? 200 : width;
	};
		
	this.getWinHeight = function(){
		var height = (Ext.lib.Dom.getViewHeight()-taskbarEl.getHeight());
		return height < 100 ? 100 : height;
	};
		
	this.getWinX = function(width){
		return (Ext.lib.Dom.getViewWidth() - width) / 2;
	};
		
	this.getWinY = function(height){
		return (Ext.lib.Dom.getViewHeight()-taskbarEl.getHeight() - height) / 2;
	};
	
	this.module = {};
	
	this.cmenu = new Ext.menu.Menu({
        items: [{
            text: '运行',
            scope: this,
            handler: function(){
            	app.createWindow(this.module);
            }
        },{
            text: '删除桌面快捷方式',
            scope: this,
            handler: function(){
            	var me = this;
            	Ext.Ajax.request({                           
        	        url: 'common.action?command=T_SYS_USERRESOURCE.updateByUserIdResourceId',
        	        method:'post',
        	        params:{"RESOURCE_ID":me.module.RESOURCE_ID,"DESKTOP":false},
        	        success:function(response, opts){
        				app.removeShortcuts(me.module);
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
            }
        }]
    });
	
    layout();

    if(desktopEl){
   	
    	desktopEl.on('click', function(e, t){
    		var dt = e.getTarget('dt', desktopEl);
            if(t && dt){
                e.stopEvent();
                var module = {};
                var str = dt.id;
                if(str){
                	var strs = str.split("*");
                    if(strs.length == 5){
                    	module.RESOURCE_ID = strs[0];
                    	module.moduleTitle = strs[1];
                    	module.moduleUrl = strs[2];
                    	module.moduleType = strs[3];
                    	module.moduleCache = strs[4];
                        
                        app.createWindow(module);
                    }else{
                    	alert("菜单数据有误！");
                    }
                }
            }
        });
    	
    	desktopEl.on('contextmenu', function(e,t){
    		var dt = e.getTarget('dt', desktopEl);
            if(t && dt){
            	e.stopEvent();
                var str = dt.id;
                if(str){
                	var strs = str.split("*");
                    if(strs.length ==5){
                    	this.module.RESOURCE_ID = strs[0];
                    	this.module.moduleTitle = strs[1];
                    	this.module.moduleUrl = strs[2];
                    	this.module.moduleType = strs[3];
                    	this.module.moduleCache = strs[4];
                    }else{
                    	alert("菜单数据有误！");
                    }
                }
                if(!this.cmenu.el){
                    this.cmenu.render();
                }
                var xy = e.getXY();
                this.cmenu.showAt(xy);
            }
        },this);
    };
};
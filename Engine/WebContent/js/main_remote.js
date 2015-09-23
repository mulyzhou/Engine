Ext.BLANK_IMAGE_URL = 'extjs/resources/images/default/s.gif';
Ext.QuickTips.init();//用于Ext的提示

var start = {
	id : 'start-panel',
	title : '欢迎使用',
	layout : 'fit',
	bodyStyle : 'padding:25px',
	html : '<img src="img/bg.jpg"/>'
};

Ext.onReady(function() {
	setTimeout(function() {
		var num = 0;
		
		Ext.get('loading').remove();
		Ext.getDom('header').style.visibility = 'visible';

		var vp = new Ext.Viewport({
			layout : 'border',
			defaults : {
				collapsible : true,
				split : true
			},
			items : [{
				xtype : 'box',
				region : 'north',
				applyTo : 'header',
				height : 30,
				split : false
			}, {
				id : 'accordion-panel',
				title:' ',
				layout : 'border',
				region : 'west',
				margins : '2 0 5 0',
				width : 200,
				collapsible:true,
				maxSize : 250,
				bodyStyle : 'background-color:#DFE8F6',
				defaults : {
					border : false
				},
				bbar : [{
					text : '开始',
					iconCls : 'icon-plugin',
					menu : new Ext.menu.Menu({
						items : [{
							text : '修改密码',
							iconCls : 'icon-info',
							handler : function() {
								//window.location = 'editUserInfo.jsp';
								var editwin = new Ext.Window({
									id:'editwin',
									title:'修改密码',
									height: 400,
								    width:600,
								    layout: 'fit',
								    html: '<iframe id="editframe" width="100%" height="100%" frameborder=0 scrolling="auto" src="editUserInfo.jsp"></iframe>'
								});
								editwin.show();
							}
						}, {
							text : '退出系统',
							iconCls : 'icon-exit',
							handler : function() {
								Ext.Msg.confirm('操作提示', '您确定要退出本系统?', function(btn) {
									if ('yes' == btn) {
										window.location = 'login.html';
									}
								});
							}
						}]
					})
				}],
				items : [{
					layout : 'accordion',
					region : 'center',
					items : [{
						title : '导航菜单',
						iconCls : 'icon-nav',
						border : false,
						items : [new Ext.flying.Tree({
							border : false,
							rootVisible : false,
							autoScroll : true,
							params : {"RESOURCE_ID" : 1},
							url : 'common.action?command=T_SYS_USERRESOURCE.selectSubMenu',
							baseConfig : {idName : 'RESOURCE_ID',textName:'RESOURCE_NAME',pidName:'PID'},
							rootNode : {RESOURCE_ID:1,RESOURCE_NAME:'根节点'},
							listeners : {
								'click' : function(n) {
									try {
										var sn = this.selModel.selNode || {};
										var data = n.attributes.data;
										if (data["RESOURCE_ADDR"] && data["FACETYPE"] !="subSystem" && data["RESOURCE_ID"] != sn.id) {
											var name = data["RESOURCE_ADDR"];
											var type = data["FACETYPE"] || "empty";
											var cache = data["CACHE"];
											//从缓存中请求
											var page = Ext.fcache.changeUrl(name,type,"",cache);
										}
									} catch (e) {
										console.log(e);
									}
								}
							}
						})]
					}]
				}]
			}, {
				id : 'content-panel',
				region : 'center',
				layout : 'card',
				margins : '2 5 5 0',
				activeItem : 0,
				border : false,
				items:[start]
			}]
		});
	}, 250);
});

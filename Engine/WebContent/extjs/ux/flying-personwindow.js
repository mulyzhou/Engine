/**
 * 选人显示的Panel
 * @author zdf 2013-5-30
 */
Ext.namespace("Ext.flying");

Ext.flying.PersonWindow = Ext.extend(Ext.Window , {
	submitUrl:'',	//接口：将选中人员保存的url
	data:{},		//包含树数据和人员列表数据
	values:'',		//对外接口,选中的Id字符串，已,分割
	fieldsetArr:[],	//form数组
	txt_search:null,//弹出window工具栏的搜索框
	tree:null,  	//人员选择树
	formPanel:null, //人员列表
	cascadeFn:function(node, checked) {   //树级联选择函数
		var me = this;
		node.expand();   
		node.attributes.checked = checked;   //树节点数据，放在attributes对象中
		//左侧树节点和右侧人员块级联选择
		Ext.each(me.formPanel.items.items,function (xitem) {
    		Ext.each(xitem.items.items,function (item) {
    			if(item.getXType() == 'checkbox'){
    				if(item.name.indexOf(node.attributes.id+"_")!=-1){	//对应树节点的人员
        				item.setValue(checked);
    				}
                }
    		});
        });
		//左侧树子节点级联选择
		node.eachChild(function(child) {   
			child.ui.toggleCheck(checked);  //ui，引用页面的dom元素 
			child.attributes.checked = checked;  
		});   
	},
	noCascadeFn:function(node, checked) {   //树非级联选择函数 
		var me = this;
		node.attributes.checked = checked;   //树节点数据，放在attributes对象中
		//左侧树节点和右侧人员块级联选择
		Ext.each(me.formPanel.items.items,function (xitem) {
    		Ext.each(xitem.items.items,function (item) {
    			if(item.getXType() == 'checkbox'){
    				if(item.name.indexOf(node.attributes.id+"_")!=-1){	//对应树节点的人员
        				item.setValue(checked);
    				}
                }
    		});
        });  
	},
	show:function(config){	
		var me = this;
		
		Ext.flying.PersonWindow.superclass.show.call(this);
		
		//展开树,绑定事件
		me.tree.on("checkchange",me.cascadeFn,me);
		me.tree.expandAll();
		
		//设置初始选中的人员
		var datas = config.dataArr;
		Ext.each(me.formPanel.items.items,function (xitem) {
    		Ext.each(xitem.items.items,function (item) {
    			if(item.getXType() == 'checkbox'){
    				item.setValue(false);
    				var isChecked = false;
    				for(var i=0;i<datas.length;i++){
    					if(item.inputValue == datas[i]){
    						isChecked = true;
    					}
    				}
    				if(isChecked){
    					item.setValue(true);
                    }
                }
    		});
        });
		
		//设置保存的url
		me.submitUrl = config.submitUrl;
	},
	refreshData:function(datas){	//刷新，根据传入的数据选中人员复选框
		var me = this;
		Ext.each(me.formPanel.items.items,function (xitem) {
    		Ext.each(xitem.items.items,function (item) {
    			if(item.getXType() == 'checkbox'){
    				item.setValue(false);
    				var isChecked = false;
    				for(var i=0;i<datas.length;i++){
    					if(item.inputValue == datas[i]){
    						isChecked = true;
    					}
    				}
    				if(isChecked){
    					item.setValue(true);
                    }
                }
    		});
        });
	},
	initComponent : function() {
		var me = this;
		
		//1 请求树数据，获取人员数据，并且拼接ext数组
	    Ext.Ajax.request({
	        url : 'common.action?command=ZZJGXX.selectAllListWithRyjbqk',
	        method : 'POST',
	        params : {},
	        async : false,
	        success: function(response, opts){
	                var responseJson = Ext.util.JSON.decode(response.responseText);
	                me.data = responseJson;
	                if(responseJson.success){
	                	var userdata = me.data.userdata;
	                    //构建formPanel中checkbox的数据
	                    for(var i=0;i<userdata.length;i++){
	                    	var employees = userdata[i].employees;
	                    	var fieldset = {
	                            xtype:'fieldset',
	                            title: userdata[i].name,
	                            collapsible:false,
	                            autoHeight:true,
	                            defaults: {width: 60},
	                            defaultType: 'checkbox',
	                            layout:"column",
	                            items :employees
	                    	};
	                    	me.fieldsetArr.push(fieldset);
	                    }
	                }else{
	                    Ext.Msg.show({
	                            title : '异常提示',
	                            msg : '获取数据出错',
	                            buttons : Ext.Msg.OK,
	                            icon : Ext.Msg.ERROR
	                    });
	                }
	        }
	    });
	  //2 创建树
		var treeData = {
			id : '0',
			iconCls : 'db-icn-world',
			text : '根目录',
			checked:false,
			children:me.data.data
		};
		me.tree = new Ext.tree.TreePanel({
	    	region : "center",
			border : false,
			rootVisible : true, //是否显示根元素
			autoScroll : true,
			root : treeData	//直接是静态树
	    });
		//3 创建人员panel
		me.formPanel = new Ext.FormPanel({
			region:'center',
	        labelWidth: 75, // label settings here cascade unless overridden
	        url:'save-form.php',
	        frame:true,
	        bodyStyle:'padding:5px 5px 0',
	        width: 350,
	        autoScroll:true,
	        items: me.fieldsetArr
	    });
		//4  创建工具栏
		me.lb_search = new Ext.form.Label({	//查询文本框文字
			text : '快速搜索：'
		});
		//查询文本框
		me.txt_search = new Ext.form.TextField({
			enableKeyEvents : true,
			listeners : {
				scope : this,
				keyup : function(txt,e){
							var searchContent = me.txt_search.getValue();
							//css选择器
							var checks = Ext.query('.x-form-cb-label');
							if(searchContent!=""){
								for(var i=0;i<checks.length;i++){
									var t = Ext.getDom(checks[i]);
								    var html = t.innerHTML;
								    if(html!="全部"&&html!="关联下级部门"){
									    if(html.indexOf(searchContent)!=-1){
									    	t.style["background"]="yellow";
									    }else{
									    	t.style["background"]="";
									    }
								    }
								}
							}else{
								for(var i=0;i<checks.length;i++){
								    var t = Ext.getDom(checks[i]);
								    t.style["background"]="";
								}
							}
				}
			}
		});
		me.toolbar = new Ext.Toolbar({// 操作工具栏
			region:'north',
			height:25,
	        items : ['-',
	                 {
	        			xtype:'checkbox',
	        			name:'cascade',
	        			boxLabel : '关联下级部门',//text
	        			inputValue:1,
	        			checked:true,
		                listeners : {
	        				check:function(t,checked){
	        					if(checked){
	        						me.tree.un('checkchange',me.noCascadeFn);
	        						me.tree.on({
	        							"checkchange":me.cascadeFn,
	        							scope:me
	        						});
	        					}else{	//取消级联
	        						me.tree.un('checkchange',me.cascadeFn,me);
	        						me.tree.on("checkchange",me.noCascadeFn,me);
	        					}
	        				}
	        			}
		            },'-',
	                 {
	        			xtype:'checkbox',
	        			name:'all',
	        			boxLabel : '全部',//text
	        			inputValue:1,
	        			listeners : {
	        				check:function(t,checked){
							    //全部选中或者全部取消
								Ext.each(me.formPanel.items.items,function (xitem) {
			                		Ext.each(xitem.items.items,function (item) {
			                			if(item.getXType() == 'checkbox'){
			                				item.setValue(checked);
				                        }
			                		});
			                        
			                    });
	        				}
	        			}
		            },'->',me.lb_search,me.txt_search]
		});
		//5 创建布局Panel
		me.center = new Ext.Panel({
		    region:'center',
		    layout : 'border',
		    frame:true,
		    items : [{
				region : 'west',
				layout : 'border',
				margins : '2 0 5 5',
				width : 200,
				minSize : 200,
				maxSize : 200,
				split : true, //分割开,可以拖动
				items : me.tree
			},me.formPanel,me.toolbar],
		    buttons:[{
		    		text:'确定',
		    		handler:function(){
	                	//获取选中的checkbox
		    			var values = '';
	                	Ext.each(me.formPanel.items.items,function (xitem) {	//item重复出现，不知道为什么。setValue()时，重复过滤
	                		Ext.each(xitem.items.items,function (item) {
	                			if(item.getXType() == 'checkbox'){
	                				if(item.checked == true){
	                					values += item.inputValue+",";
	                                }
		                        }
	                		});
	                    });
                        //1 设置选中的值和名称
                		me.values = values.substring(0,values.length-1);
                		//2 将选中的人员Id字符串提交保存
                	    Ext.Ajax.request({
                	        url : me.submitUrl,//'common.action?command=T_OA_GROUP.insertUsers&GROUP_ID=104',
                	        method : 'POST',
                	        params : {userIds:me.values},
                	        async : false,
                	        success: function(response, opts){
                	                var responseJson = Ext.util.JSON.decode(response.responseText);
                	                me.data = responseJson;
                	                if(responseJson.success){

                	                }else{
                	                    Ext.Msg.show({
                	                            title : '异常提示',
                	                            msg : '获取数据出错',
                	                            buttons : Ext.Msg.OK,
                	                            icon : Ext.Msg.ERROR
                	                    });
                	                }
                	        }
                	    });
                		me.hide();
		    		}
		    	},{
		    		text:'取消',
		    		handler:function(){
		    			me.hide();
		    		}
		    	}]
		});
		//6 创建弹出窗口
		var winTemplate = {
			title : "选择人员",
			layout : 'fit',
			width : 800,
			height : 500,
			closeAction : 'hide',	
			plain : true,
			items : me.center
		};
		Ext.applyIf(me,winTemplate);
		me.closeAction = 'hide';	//没有赋值成功，重新赋值
		
		Ext.flying.PersonWindow.superclass.initComponent.call(this);
	}
});
	
Ext.reg('personwindow',Ext.flying.PersonWindow);
/**
 * 选人组件
 * 包括一个文本控件，一个隐藏域，一个按钮
 * 控件初始化时，Ajax调用数据，文件控件setValue时，重新构建数据，将人员Id字符串变换成人员Name字符串，隐藏域进行赋值。
 * @author zjx 2012-10-26
 * 示例数据
var data = {
	"userdata" : [ {
		"id" : "104",
		"name" : "航修厂",
		"employees" : [ {
			"inputValue" : "7",
			"boxLabel" : "张三",
			"name" : "104_7",
			"hideLabel" : true
		}, {
			"inputValue" : "8",
			"boxLabel" : "李四",
			"name" : "104_8",
			"hideLabel" : true
		}, {
			"inputValue" : "105",
			"boxLabel" : "zdf2",
			"name" : "104_105",
			"hideLabel" : true
		}, {
			"inputValue" : "107",
			"boxLabel" : "zjx1",
			"name" : "104_107",
			"hideLabel" : true
		}, {
			"inputValue" : "108",
			"boxLabel" : "zjx2",
			"name" : "104_108",
			"hideLabel" : true
		}, {
			"inputValue" : "114",
			"boxLabel" : "zjx8",
			"name" : "104_114",
			"hideLabel" : true
		}, {
			"inputValue" : "115",
			"boxLabel" : "zjx9",
			"name" : "104_115",
			"hideLabel" : true
		}, {
			"inputValue" : "116",
			"boxLabel" : "zjx10",
			"name" : "104_116",
			"hideLabel" : true
		}, {
			"inputValue" : "117",
			"boxLabel" : "zjx11",
			"name" : "104_117",
			"hideLabel" : true
		}, {
			"inputValue" : "118",
			"boxLabel" : "zjx12",
			"name" : "104_118",
			"hideLabel" : true
		}, {
			"inputValue" : "119",
			"boxLabel" : "zjx13",
			"name" : "104_119",
			"hideLabel" : true
		} ]
	}, {
		"id" : "105",
		"name" : "组织部",
		"employees" : [ {
			"inputValue" : "104",
			"boxLabel" : "zdf1",
			"name" : "105_104",
			"hideLabel" : true
		}, {
			"inputValue" : "106",
			"boxLabel" : "zdf3",
			"name" : "105_106",
			"hideLabel" : true
		}, {
			"inputValue" : "109",
			"boxLabel" : "zjx3",
			"name" : "105_109",
			"hideLabel" : true
		} ]
	}, {
		"id" : "108",
		"name" : "主任室",
		"employees" : [ {
			"inputValue" : "112",
			"boxLabel" : "zjx6",
			"name" : "108_112",
			"hideLabel" : true
		} ]
	}, {
		"id" : "109",
		"name" : "副主任室",
		"employees" : [ {
			"inputValue" : "113",
			"boxLabel" : "zjx7",
			"name" : "109_113",
			"hideLabel" : true
		} ]
	}, {
		"id" : "106",
		"name" : "生活部",
		"employees" : [ {
			"inputValue" : "110",
			"boxLabel" : "zjx4",
			"name" : "106_110",
			"hideLabel" : true
		} ]
	}, {
		"id" : "107",
		"name" : "后勤部",
		"employees" : [ {
			"inputValue" : "111",
			"boxLabel" : "zjx5",
			"name" : "107_111",
			"hideLabel" : true
		} ]
	}, {
		"id" : "110",
		"name" : "维修部",
		"employees" : []
	}, {
		"id" : "111",
		"name" : "小组1",
		"employees" : []
	}, {
		"id" : "112",
		"name" : "小组2",
		"employees" : []
	} ],
	"data" : [ {
		"id" : "104",
		"text" : "航修厂",
		"checked" : false,
		"expanded" : true,
		"children" : [ {
			"id" : "105",
			"text" : "组织部",
			"checked" : false,
			"expanded" : true,
			"children" : [ {
				"id" : "108",
				"text" : "主任室",
				"checked" : false,
				"leaf" : true
			}, {
				"id" : "109",
				"text" : "副主任室",
				"checked" : false,
				"leaf" : true
			} ]
		}, {
			"id" : "106",
			"text" : "生活部",
			"checked" : false,
			"leaf" : true
		}, {
			"id" : "107",
			"text" : "后勤部",
			"checked" : false,
			"leaf" : true
		}, {
			"id" : "110",
			"text" : "维修部",
			"checked" : false,
			"expanded" : true,
			"children" : [ {
				"id" : "111",
				"text" : "小组1",
				"checked" : false,
				"leaf" : true
			}, {
				"id" : "112",
				"text" : "小组2",
				"checked" : false,
				"leaf" : true
			} ]
		} ]
	} ],
	"success" : true
};
 */
var config = {
	data:{},		//包含树数据和人员列表数据
	submitName:'',	//提交表单的名称
	setValue : function(v){ //初始化完成后，才执行setValue方法*********value可能有重复值
		var me = this;
		
		//将Id换成名称;将初始化的用户checkbox选中
		var idArr = v.split(',');	//初始化的人员Id数组
		var userdata = me.data.userdata;
		var values = '';//选中人员Id
		var names = '';	//选中的人员名称
		
        //构建formPanel中checkbox的数据
		me.fieldsetArr.clear();
        for(var i=0;i<userdata.length;i++){
        	var employees = userdata[i].employees;
        	for(var j=0;j<employees.length;j++){	//初始化人员checkbox是否选中
        		var checked = false;
        		for(var k=0;k<idArr.length;k++){
        			if(employees[j]["inputValue"]==idArr[k]){	//该checkbox是选中节点
        				checked = true;
        				values = values + employees[j]["inputValue"] + ",";	//选中的人员Id
        				names = names + employees[j]["boxLabel"] + ",";	//选中的人员名称
        				break;
        			}
        		}
        		employees[j]["checked"] = checked;
        	}
        	var fieldset = {
                xtype:'fieldset',
                title: userdata[i].name,
                collapsible:false,
                autoHeight:true,
                defaults: {width: 60},
                defaultType: 'checkbox',
                layout:"column",
                //layoutConfig:{columns:6},
                items :employees
        	};
        	me.fieldsetArr.push(fieldset);
        }
        
        me.values = values.substring(0,values.length-1);
        me.names = names.substring(0,names.length-1);
		
		//同步数据到隐藏域
		//var input = Ext.getDom(Ext.query('#id_'+me.submitName)[0]);	//给隐藏域指定了一个唯一Id
		me.input.value = me.values ;
		
		Ext.flying.PersonSelection.superclass.setValue.call(this,me.names);

	},
	names:'',		//对外接口，选中的Name字符串，已，分割
	values:'',		//对外接口,选中的Id字符串，已,分割
	btn:null,		//选择按钮
	input:null,		//隐藏域
	fieldsetArr:[],	//form数组
	txt_search:null,//弹出window工具栏的搜索框
	tree:null,  	//人员选择树
	formPanel:null, //人员列表
	win:null,		//弹出窗口
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
	clickFn:function(){	
		var me = this;

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
				//title : '目录',
				//collapsible : true,	//可以关闭,在标题上按钮
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
		    			var names = '';
	                	Ext.each(me.formPanel.items.items,function (xitem) {	//item重复出现，不知道为什么。setValue()时，重复过滤
	                		Ext.each(xitem.items.items,function (item) {
	                			if(item.getXType() == 'checkbox'){
	                				if(item.checked == true){
	                					values += item.inputValue+",";
	                					names += item.boxLabel+",";
	                                }
		                        }
	                		});
	                    });
                        //设置选中的值和名称
                		me.values = values.substring(0,values.length-1);
                		me.names = names.substring(0,names.length-1);
                		me.win.hide();
                		//重新赋值
                   		me.setValue(me.values);
		    		}
		    	},{
		    		text:'取消',
		    		handler:function(){
		    			me.win.hide();
		    		}
		    	}]
		});
		//6 创建弹出窗口
		me.win = new Ext.Window({
			title : "选择人员",
			layout : 'fit',
			width : 800,
			height : 500,
			closeAction : 'hide',
			plain : true,
			items : me.center
		});
		me.win.show();
		//7 展开树,绑定事件
		me.tree.on("checkchange",me.cascadeFn,me);
		me.tree.expandAll();

	},
	initComponent : function() {
		var me = this;
		//更改输入框的name
		me.submitName = me.name;
		me.name = "name_"+me.name;
		me.disabled = false;	//文本框不可更改
		
		Ext.flying.PersonSelection.superclass.initComponent.call(this);
		
		//1 请求树数据，获取人员数据，并且拼接ext数组
	    Ext.Ajax.request({
	        url : 'common.action?command=T_OA_INFO.selectDepartmentList',
	        method : 'POST',
	        params : {},
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
	},
	onRender : function(ct, position) {
		Ext.flying.PersonSelection.superclass.onRender.call(
				this, ct, position);

		var me = this;
		//绑定input事件
		me.on({
			"focus":me.clickFn,
			scope:me
		});
		
		// 添加按钮
		me.inputTemplate =  new Ext.Template(	//添加按钮模板
                '<input type="hidden" name="{2}" value="" />');
//		me.buttonTemplate =  new Ext.Template(	//添加按钮模板
//                '<table border="0" cellpadding="0" cellspacing="0" class="x-btn-wrap"><tbody><tr>',
//                '<td><em unselectable="on"><button class="x-btn-text" type="{1}">{0}</button></em></td>',
//                "</tr></tbody></table>");
		var btn,input;
		var targs = [ '选择人员', 'button',me.submitName];
		input = me.inputTemplate.append(ct, targs, false);//false返回htmlElement
		me.input = input;
		
//		btn = me.buttonTemplate.append(ct, targs, true);//true返回ExtElement
//		me.btn = btn;
//		//绑定添加事件
//		me.btn.on({
//			"click":me.clickFn,
//			scope:me
//		});
	}
};

Ext.flying.PersonSelection = Ext.extend(Ext.form.TextArea,config);
	
Ext.reg('personselection',Ext.flying.PersonSelection);
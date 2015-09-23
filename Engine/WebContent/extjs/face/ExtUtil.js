Ext.apply(Ext, {
	futil : {
		/**处理Grid的列*/
		"handleColumn" : function(columns,grid){
			var newCol = new Array();
			columns = columns || new Array();
			
			for ( var m = 0; m < columns.length; m++) {// 构建表格列
				if(columns[m]["isColumn"] == false){
					continue;
				}
							
				newCol.push(this.columnPack(columns[m],grid));// 包装表格列
			}
			
			return newCol;
		},
		
		/** 列项包装方法*/
		"columnPack" : function(columnObj,grid) {

			var columnTemplate = {// 默认值
				sortable : true,// 可排序
				menuDisabled : true,// 无需表头菜单
				width : 100
			// 默认宽度100
			};
			
			this.assign(columnTemplate, columnObj);// 进行赋值操作

			if (columnTemplate["isPk"]) {// 对主键进行处理
				columnTemplate["hidden"] = true;// 隐藏主键列
			}

			if (columnTemplate["renderer"]) {// 处理列自定义的问题
				if(grid[columnTemplate["renderer"]]){
					columnTemplate["renderer"] = grid[columnTemplate["renderer"]];
				}else if(grid.parent != null && grid.parent[columnTemplate["renderer"]]){
					columnTemplate["renderer"] = grid.parent[columnTemplate["renderer"]];
				}
				
			}

			return columnTemplate;
		},
		
		/**属性置换方法*/
		"assign" : function(target, source) {// 赋值，将source中的所有属性赋值到target对象中
			source = source || {};
			for ( var name in source) {// 初始化属性
				target[name] = source[name];
			}
		},
		
		/**构建grid的Model*/
		"handleModel" : function(columns){
			var field = {};
			var modelArray = new Array();
			columns = columns || new Array();
			
			for ( var i = 0; i < columns.length; i++) {//构建查询项，实体对象
				if (field[columns[i].dataIndex] == undefined) {// 构建实体
					field[columns[i].dataIndex] = columns[i].dataIndex;
					modelArray.push(this.modelPack(columns[i]));// 包装实体对象
				}
			}
			
			return modelArray;
		},
		
		/** 对象包装方法*/
		"modelPack" : function(columnObj) {
			var model = {};
			model.name = columnObj.dataIndex || '';// 名称
			model.type = columnObj.type || 'string';// 类型

			return model;
		},
		
		
		
		
		/** 构建表单方法*/
		"handleForm" : function(columns,formColumn,form) {// 处理表单方法
			var formArray = new Array();
			
			var centerColumnForm = new Array();
			var leftColumnForm = new Array();
			var rightColumnForm = new Array();
			
			formColumn = formColumn || 1;
			
			for ( var i = 0; i < columns.length; i++) {// 遍历
				if (columns[i]["isPk"]) {// 对主键进行处理
					continue;
				}
				
				if (columns[i]["isForm"] == false) {// 不做为表单项
					continue;
				}
				/** 按列显示 */
				if(formColumn == 1){//分成一列显示
					centerColumnForm.push(this.formPack(columns[i],form));
					
				}else if(formColumn == 2){//分成两列显示
					var order = columns[i]["order"];
					if(order == null){
						if (i % 2 == 1) {// 奇数放左侧
							leftColumnForm.push(this.formPack(columns[i],form));
						} else {// 偶数放右侧
							rightColumnForm.push(this.formPack(columns[i],form));
						}
					}else{
						if (order % 2 == 1) {// 奇数放左侧
							leftColumnForm.push(this.formPack(columns[i],form));
						} else {// 偶数放右侧
							rightColumnForm.push(this.formPack(columns[i],form));
						}
					}
				}
			}
			
			/** 进行排序显示*/
			if(formColumn == 1){//分成一列显示
				centerColumnForm.sort(this.sortOrder);//排序
				formArray.push(centerColumnForm);
			}else if(formColumn == 2){//分成两列显示
				leftColumnForm.sort(this.sortOrder);//左侧排序
				rightColumnForm.sort(this.sortOrder);//右侧排序
				formArray.push(leftColumnForm);
				formArray.push(rightColumnForm);
			}
			
			return formArray;
		},
		/** 排序算法*/
		"sortOrder" : function sortNumber(first, second){
		  return first.order - second.order;
		},
		
		/** 表单对象包装方法*/
		"formPack" : function(formObj,form) {// 表单项包装方法
			var formTemplate = {};// 默认值
			var packFormObj = {};

			if (formObj["xtype"] != undefined) {// 是否可以为空
				packFormObj["xtype"] = formObj["xtype"];
			}
			
			packFormObj["fieldLabel"] = formObj["header"];// 标题
			packFormObj["name"] = formObj["dataIndex"];// 名称

			if (formObj["allowBlank"] != undefined) {// 是否可以为空
				packFormObj["allowBlank"] = formObj["allowBlank"];
			}
			if (formObj["maxLength"] != undefined) {// 最大长度
				packFormObj["maxLength"] = formObj["maxLength"];
			}
			if (formObj["maxValue"] != undefined) {// 最大值
				packFormObj["maxValue"] = formObj["maxValue"];
			}
			if (formObj["readOnly"] != undefined) {//正则判断
				packFormObj["readOnly"] = formObj["readOnly"];
			}
			
			if (formObj["regex"] != undefined) {//正则判断
				packFormObj["regex"] = formObj["regex"];
			}
			
			if (formObj["regexText"] != undefined) {// 正则判断提示
				packFormObj["regexText"] = formObj["regexText"];
			}
			
			if (formObj["anchor"] != undefined) {//改变表单的宽度
				packFormObj["anchor"] = formObj["anchor"];
			}
			
			if (formObj["dateConfig"] != undefined) {// my97的配置属性
				packFormObj["dateConfig"] = formObj["dateConfig"];
			}
			
			if (formObj["items"] != undefined) {// 是否有子项
				packFormObj["items"] = formObj["items"];
			}
			
			if (formObj["inputType"] != undefined) {// 输入类型
				packFormObj["inputType"] = formObj["inputType"];
			}

			if (formObj["value"] != undefined) {// 默认值
				packFormObj["value"] = formObj["value"];
			}

			if (formObj["msgTarget"] != undefined) {// 提示显示的位置
				packFormObj["msgTarget"] = formObj["msgTarget"];
			}
			
			/** 更改操作函数的作用域，换做当前对象 */
			if (formObj["validator"] != undefined) {//验证操作
				packFormObj["validator"] = form[formObj["validator"]].createDelegate(form);
			}
			
			if (formObj["listeners"] != undefined) {// 添加事件
				var lis = formObj["listeners"];
				for(var name in lis){
					if(typeof(lis[name]) == "function"){
						lis[name] = lis[name].createDelegate(form);
					}
				}
				
				packFormObj["listeners"] = lis;
			}
			
			if (formObj["order"] != undefined) {// 排序
				packFormObj["order"] = formObj["order"];
			}else{
				packFormObj["order"] = 9999;
			}		
			
			/** 根据不同类型构建不同的表单控件 */
			if (formObj["xtype"] == "combo") {// 如果是下拉列表框类型
				formTemplate = this.comboPack(formObj);
			} else if (formObj["xtype"] == "datefield") {
				formTemplate = this.datePack(formObj);
			} else if(formObj["xtype"] == "textarea"){
				formTemplate = this.textAreaPack(formObj);
			} else if(formObj["xtype"] == "numberfield"){
				// 对于类型的处理的预留区
				if (formObj["decimalPrecision"] != undefined) {// 提示显示的位置
					packFormObj["decimalPrecision"] = formObj["decimalPrecision"];
				}
			} else if(formObj["xtype"] == "upload"){
				packFormObj["delFileFn"] = formObj["delFileFn"];
				packFormObj["submitName"] = formObj["submitName"];
			}

			this.assign(formTemplate, packFormObj);// 进行赋值操作

			return formTemplate;
		},
		
		/**下来列表项包装方法*/
		"comboPack" : function(formObj) {
			var comboTemplate = {};

			if (formObj["url"] == undefined) {
				comboTemplate = {
					xtype : 'combo',
					store : new Ext.data.SimpleStore(
							{
								fields : [ formObj["displayField"],
										formObj["valueField"] ],
								data : formObj["data"]
							}),
					displayField : formObj["displayField"],
					valueField : formObj["valueField"],
					typeAhead : true,
					mode : 'local',
					value : 0,
					triggerAction : 'all',
					fieldLabel : formObj["header"],
					hiddenName : formObj["dataIndex"],
					forceSelection : true,
					allowBlank : false,
					maxLength : 50
				};
			} else {
				comboTemplate = {
					xtype : 'combo',
					fieldLabel : formObj["header"],
					hiddenName : formObj["dataIndex"],
					valueField : formObj["valueField"],
					displayField : formObj["displayField"],
					mode : 'remote',
					pageSize : 10,
					minChars : 1,
					store : new Ext.data.Store({
						autoLoad : true,
						url : formObj["url"],
						reader : new Ext.data.JsonReader({
							root : 'data',
							totalProperty : "total"
						}, [ {
							name : formObj["displayField"]
						}, {
							name : formObj["valueField"]
						} ])
					}),
					forceSelection : true,
					selectOnFocus : true,
					triggerAction : 'all',
					loadingText : '加载中...',
					listeners : {
						"beforequery" : function(event){
							event.combo.getStore().baseParams["searchField"] = formObj["displayField"],
							event.combo.getStore().baseParams["searchValue"] = event.combo.getRawValue();					}
					}
				};
			}

			return comboTemplate;
		},

		/**时间包装方法*/
		"datePack" : function(formObj) {
			var dateTemplate = {
				xtype : 'datefield',
				fieldLabel : formObj["header"],
				format : formObj["format"] == undefined ? "Y-m-d"
						: formObj["format"],
				name : formObj["dataIndex"]
			};

			return dateTemplate;
		},
		
		/**文本框包装方法*/
		"textAreaPack" : function(formObj){
			var textAreaTemplate = {
					xtype : 'textarea',
					fieldLabel : formObj["header"],
					name : formObj["dataIndex"]
				};

				return textAreaTemplate;
		},
		
		
		
		
		/** 创建操作按钮方法*/
		"handleButton" : function(btns,toolbar) {// 操作处理方法
			var buttonsArray = new Array();
			
			btns = btns || new Array();
			for ( var i = 0; i < btns.length; i++) {// 遍历
					buttonsArray.push(this.buttonPack(btns[i],toolbar));
			}
			
			return buttonsArray;
		},
		/** 操作按钮包装方法*/
		"buttonPack" : function(buttonObj,toolbar) {
			var buttonTemplate = {};//按钮模板
			/** 如果是一个外部文件，先加载外部文件 */
			if(buttonObj["link"] != null){
				var src = buttonObj["link"];
				var type = buttonObj["type"] || "common";
				if(toolbar.parent){
					toolbar.parent[buttonObj["linkName"]] = Ext.fcache.push(src,type);
					toolbar.parent[buttonObj["linkName"]].parent = toolbar.parent;
				}else{
					toolbar[buttonObj["linkName"]] = Ext.fcache.push(src,type);
					toolbar[buttonObj["linkName"]].parent = toolbar;
				}
			}

			/** 操作预定义类型处理 */
			if (buttonObj["btype"] == "add") {
				this.assign(buttonTemplate, {
					text : '添加',
					iconCls : 'user-add',
					scope:toolbar,
					handler : toolbar.clickAdd
				});
			} else if (buttonObj["btype"] == "modify") {
				this.assign(buttonTemplate, {
					text : '修改',
					iconCls : 'user-edit',
					scope:toolbar,
					handler : toolbar.clickModify
				});
			} else if (buttonObj["btype"] == "delete") {
				this.assign(buttonTemplate, {
					text : '删除',
					iconCls : 'user-delete',
					scope:toolbar,
					handler : toolbar.clickDelete
				});
			} else if (buttonObj["btype"] == "refresh") {
				this.assign(buttonTemplate, {
					text : '刷新',
					iconCls : 'refresh',
					scope:toolbar,
					handler : toolbar.clickRefresh
				});
			} else if (buttonObj["btype"] == "download") {
				this.assign(buttonTemplate, {
					text : '导出',
					iconCls : 'download',
					scope:toolbar,
					handler : toolbar.clickDownload
				});
			}else{
				if(buttonObj["link"] != null && buttonObj["handler"] == null){
					buttonObj["scope"] = toolbar,
					buttonObj["handler"] = toolbar.clickShow;
				}
			}
			
			/** 更改操作函数的作用域，换做当前对象 */
			if (buttonObj["handler"]) {
				if(toolbar.parent){
					buttonObj["handler"] = buttonObj["handler"].createDelegate(toolbar.parent);
				}else{
					buttonObj["handler"] = buttonObj["handler"].createDelegate(toolbar);
				}
				
			}
			
			this.assign(buttonTemplate, buttonObj);
			
			return buttonTemplate;// 返回重构的btn
		}
	}	
})
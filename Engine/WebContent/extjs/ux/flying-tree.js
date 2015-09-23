Ext.namespace("Ext.flying");

Ext.flying.Tree = Ext.extend(Ext.tree.TreePanel, {
	checked : false,
	checkData : [],
	baseConfig : {},
	rootNode : {},
	data : [],
	params : {},
    initComponent: function(){
		this.initData();//初始化数据
		
		this.initTree();//初始化树
		
		Ext.flying.Tree.superclass.initComponent.call(this);
				
		this.flyingCreateTree();//创建树
		
		if(this.checked){
			this.bindCheckChange();
		}
	},
	initData : function(){
		var me = this;
		
		/**后台请求树Array数据*/
		Ext.Ajax.request({
			url : me.url,
			params: me.params,
			method:'GET',
			async: false,
			success : function(response,options){
				var obj = Ext.util.JSON.decode(response.responseText);
				if(obj.success){
					me.data = obj["data"] || [];
				}else{
					Ext.Msg.show({
						title : '异常提示',
						msg : obj.msg,
						buttons : Ext.Msg.OK,
						icon : Ext.Msg.ERROR
					});
				}

			},
			fail : function(response, options) {
				var obj = Ext.util.JSON.decode(response.responseText);
				Ext.Msg.show({
					title : '错误提示',
					msg : obj.msg,
					buttons : Ext.Msg.OK,
					icon : Ext.Msg.ERROR
				});
			}
		});
	},
	initTree : function(){
		var me = this;
		
		me.rootNode["id"] = me.rootNode[me.baseConfig["idName"]];
		me.rootNode["text"] = me.rootNode[me.baseConfig["textName"]];
		
		if(me.baseConfig["iconName"]){
			me.rootNode["icon"] = me.rootNode[me.baseConfig["iconName"]];
		}

		me.rootNode["data"] = me.rootNode;
		
		if(me.checked){
			me.rootNode["data"]["checked"] = false;
			if (me.checkData.length > 0) {
				me.rootNode["data"]["checked"] = true;
			}
		}
		me.root = new Ext.tree.TreeNode(me.rootNode);
	},
	
	flyingCreateTree : function(){
		var me = this;
		
		var map = {};
		
		me.rootNode.children = [];
		map["k" + me.rootNode[me.baseConfig["idName"]]] = me.rootNode;
		
		for (var i=0;i<me.data.length;i++) {//构建树数据集合 
			me.data[i].children = [];
            map[("k" + me.data[i][me.baseConfig["idName"]])] = me.data[i];
        }
        
        for (var i=0;i<me.data.length;i++) {//构建树结构
            var ppid = "k" + me.data[i][me.baseConfig["pidName"]];
            if (map[ppid] == null){	//可以接受多棵树
				continue;
			}				
			map[ppid].children.push(map[("k" + me.data[i][me.baseConfig["idName"]])]);
        }
		
		var rootChildren = [];
		if(me.getRootNode().hasChildNodes()){
			me.getRootNode().eachChild(function(n){
				rootChildren.push(n);
			});
		}
		
		for(var i =0;i<rootChildren.length;i++){
			rootChildren[i].remove();
		}
		
		for(var i =0;i<map["k" + me.rootNode.id].children.length;i++){
			me.getRootNode().appendChild(this.flyingCreateNode(map["k"+me.rootNode[me.baseConfig["idName"]]].children[i]));
		}
	},
	flyingCreateNode : function(node){
		var me = this;
		
		node["id"] = node[me.baseConfig["idName"]];
		node["text"] = node[me.baseConfig["textName"]];
		
		if(me.baseConfig["iconName"]){
			node["icon"] = node[me.baseConfig["iconName"]];
		}
		
		node["data"] =node;
		if(me.checked){
			node["checked"] =false;
			for(var m = 0;m<me.checkData.length;m++){
				if(node[me.baseConfig["idName"]] == me.checkData[m]){
					node["checked"] = true;
					break;
				}
			}
		}

		var n = new Ext.tree.TreeNode(node);
		if(node.children != null && node.children.length >0){
			for(var i =0;i<node.children.length;i++){
				n.appendChild(this.flyingCreateNode(node.children[i]));
			}
		}
		
		return n;
	},
	checkedRefresh : function(checkData){
		this.checkData = checkData;
		if(this.getRootNode().getUI().checkbox != null){
			if(checkData.length > 0){
				this.getRootNode().getUI().checkbox.checked = true;
			}else{
				this.getRootNode().getUI().checkbox.checked = false;
			}
		}
		this.localRefresh();
	},
	localRefresh : function(data){
		if(data == null){
			this.flyingCreateTree();
		}else{
			this.data = data;
			this.flyingCreateTree();
		}
	},
	remoteRefresh : function(){
		this.initData();
		this.flyingCreateTree();
	},
	bindCheckChange:function(){	//绑定复选框选中事件
		var me = this;
		this.on('checkchange',function(node, checked){
			
			function checkedChildNodes(node, checked) {
				node.attributes.checked = checked;
				node.getUI().checkbox.checked=checked;
				if (node.hasChildNodes) {

					var cn = node.childNodes;// 取出所有子节点
					for (var i=0;i<cn.length;i++) {
						checkedChildNodes(cn[i], checked);
					}
				}
			}
			
			function getChildChecked(node) {

				if (node.hasChildNodes) {

					var cn = node.childNodes;// 取出所有子节点
					for (var i=0;i<cn.length;i++) {

						var chked;
						try {
							chked = cn[i].attributes.checked;
						} catch (err) {
							//不作处理，直接跳过
						}

						if (chked == true) {
							return true;
						}
					}
				}

				return false;
			}
			
			function checkedParentNodes(node, checked) {

				var pNode = node.parentNode;
				if ((pNode == 'undefined') || (pNode == null)) {
					return;
				}
				if ((pNode != 'undefined') && (pNode != null)&&(pNode.id==me.getRootNode().id)) {	//判断是不是根节点
					//判断根节点下是否还有子节点
					if((checked==false)&&getChildChecked(pNode)){	//不勾选复选框，而且父节点还有儿子节点
						return;
					}
					if(pNode.getUI().checkbox != null){
						pNode.getUI().checkbox.checked = checked;
					}
					return;
				}
				/*
				 * if (pNode.raw != null) { pNode.raw.checked = checked; }
				 */
				if((checked!=false)||!getChildChecked(pNode)){
					
					if (pNode.attributes != null) {
						pNode.attributes.checked = checked;
						pNode.getUI().checkbox.checked=checked;
					}
				}				
				checkedParentNodes(pNode, checked);

			}
			/**
			 * 展开所有子孙节点
			 */
			function expandChildNodes(node) {
				node.expand();
				if (node.hasChildNodes) {

					var cn = node.childNodes;// 取出所有子节点
					for (var i=0;i<cn.length;i++) {
						expandChildNodes(cn[i]);
					}
				}
			}
			
			expandChildNodes(node); //先将选中节点及它的子孙节点展开
			checkedChildNodes(node, checked);
			checkedParentNodes(node, checked);
		});
	},
	getCheckedId:function(){	//获得选中checkbox的值
		var nodes = this.getChecked();
		var datas = "";
		for(var i=0;i<nodes.length;i++){
			if(nodes[i].id == this.root.id){
				continue;
			}
			datas += nodes[i].id + ",";
		}
		
		if(datas.length>0){
			datas = datas.substr(0,datas.length-1);
		}
		
		return datas;
	}
});
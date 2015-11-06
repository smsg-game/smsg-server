
Ext.onReady(function(){

	var data_record = new Ext.data.Record.create([
		{name: 'id'},
		{name: 'cup_name'},
		{name: 'cup_key'},
		{name: 'cup_icon'},
		{name: 'is_youth'},
		{name: 'created_time'},
	]);
 	
	var data_read = new Ext.data.JsonReader({totalProperty: 'total', root: 'infos', id: 'id'}, data_record);
	
	var store = new Ext.data.Store({proxy: new Ext.data.HttpProxy({url: '/admin/cup_list_json/'}),
        						    reader: data_read,
        							remoteSort: true ,
        					       });
        							         
    var data_grid_panel = new Ext.grid.GridPanel({
        store: store,
        width: 600,
        height: 645,
        region:'center',
        loadMask: true,
        columns: [
           new Ext.grid.RowNumberer()
        ,{
            id: 'id',
            header: '编号',
            dataIndex: 'id',
            hidden: true,
            width: 55,
        },{
            id: 'cup_name',
            header: '奖杯名',
            dataIndex: 'cup_name',
            width: 120,
        },{
            id: 'cup_key',
            header: '奖杯id',
            dataIndex: 'cup_key',
            width: 120,
        },{
            id: 'cup_icon',
            header: '奖杯图标',
            dataIndex: 'cup_icon',
            width: 200,
            renderer: function(value){
                var html = "<img src=\"/site_media/images/cup/" + value + "\"/>";
                return html;
            }
        },{
            id: 'is_youth',
            header: '球队类型',
            dataIndex: 'is_youth',
            width: 90,
            align: 'center',
            renderer: function(value){
              if(value==1){
                return "<font color=\"green\">街头</font>";
              }else{
                return "<font color=\"black\">职业</font>";
              }
            }
        },{
            id: 'created_time',
            header: '创建时间',
            dataIndex: 'created_time',
            width: 150,
        },{
            id: 'edit',
            align: 'center',
            header: '编辑',
            dataIndex: 'cup_key',
            width: 100,
            renderer: function(value){
              var href = "<a href=\"javascript:void(0);\" onclick=\"javascript:edit('" + value + "');\">edit</a>";
              return href;
            }
        }],
        tbar: [
          {
        	ref: '../addBtn',
            iconCls: 'task-add',
            text: '创建奖杯',
            handler: function(){
			    add_cup();
            }
        }],
        bbar: new Ext.PagingToolbar({
	        pageSize: 20,
	        store: store,
	        displayInfo: true,
	        displayMsg: '显示第 {0} 条到 {1} 条记录，一共 {2} 条',
	        emptyMsg: "没有记录"
	    })
      
    });
    var downurl_panel = new Ext.Panel({
        title: '奖杯管理',
        layout: 'border',
        renderTo: 'data_div',
        layoutConfig: {
            columns: 1
        },
        height:650,
        items: [data_grid_panel]
    });
    
    store.load();
    
    edit = function(key){
      
       Ext.Ajax.request({ 
			url : '/admin/get_cup_json/',
			sync : true,
			params : { 
			  cup_key: key,
			},
		    success : function(response) { 
		        var data = response.responseText;
			    var info = Ext.util.JSON.decode(data);
			    show_edit_window(info);
			}, 
		    failure : function(response){ 
			    Ext.Msg.show({ 
			      title : '请求出错', 
			      msg : '读取信息出错!', 
			      icon : Ext.Msg.ERROR, 
			      buttons : Ext.Msg.OK 
			    })
			}
	    }) 
    };
    
   add_cup = function(){
       show_edit_window(null)
   };
    
   show_edit_window = function(info){
    	    	
    	var edit_form = new Ext.FormPanel({
	        labelWidth: 65, 
	        url: '/admin/cup_update/',
	        width: 450,
	        height: 150,
	        frame:true,
	        defaultType: 'textfield',
	        defaults: {width: 280},
	        items: [{
	             xtype: 'hidden',
		         name: 'id',
		         value: info!=null?info.id:'',
	           },{
	             fieldLabel: '奖杯名称',
		         name: 'cup_name',
		         value: info!=null?info.cup_name:'',
	           },{
	             fieldLabel: '奖杯ID',
		         name: 'cup_key',
		         value: info!=null?info.cup_key:'',
	           },{
	             fieldLabel: '奖杯图标',
		         name: 'cup_icon',
		         value: info!=null?info.cup_icon:'',
	           },{
	             xtype: 'checkbox',
	             fieldLabel: '街头',
	             style: 'margin-top:6px;',
		         name: 'is_youth',
		         width: 25,
		         checked: info!=null?info.is_youth==1:false,
	           }
	        ],
	        buttons: [{
	            text: '保存',
	            handler: function(){
	               edit_form.form.submit({
	                 waitMsg: '保存中...',
	                 success: function(re, v){
	                 	 edit_form.form.reset();
	                 	 edit_window.close();
	                 	 Ext.Msg.show({ 
					       itle : '保存成功', 
					       msg : '保存成功', 
					       icon : Ext.Msg.SUCCESS, 
					       buttons : Ext.Msg.OK 
					     })
					     store.load();
	                 },
	                 failure: function(){
	                 	 Ext.Msg.show({ 
					       itle : '保存失败', 
					       msg : '保存失败', 
					       icon : Ext.Msg.ERROR, 
					       buttons : Ext.Msg.OK 
					     })
	                 }
	               });
	            }
	        },{
	            text: '取消',
	            handler: function(){
	               edit_window.close();
	            }
	        }],
	        listeners: {
               'show':function(){
                  this.center(); //屏幕居中
               }
             }
	    });
	    
	    var edit_window = new Ext.Window({  
            title: '奖杯编辑',  
            width: 450,  
            height: 180,  
            closable: true,  
            closeAction: 'close',  
            resizable: false,   
            items: edit_form
        });  

        edit_window.show(); 
   };
});
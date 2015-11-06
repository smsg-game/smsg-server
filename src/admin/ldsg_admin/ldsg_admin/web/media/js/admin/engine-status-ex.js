
Ext.onReady(function(){

	var data_record = new Ext.data.Record.create([
		{name: 'id'},
		{name: 'name'},
		{name: 'status'},
		{name: 'info'},
		{name: 'cmd'},
		{name: 'updated_time'},
	]);
 	
	var data_read = new Ext.data.JsonReader({totalProperty: 'total', root: 'infos', id: 'id'}, data_record);
	
	var store = new Ext.data.Store({proxy: new Ext.data.HttpProxy({url: '/admin/engine_status_json/'}),
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
            id: 'name',
            header: '',
            dataIndex: 'name',
            width: 120,
        },{
            id: 'status',
            header: '状态',
            dataIndex: 'status',
            width: 70,
            renderer: function(value){
              switch(value){
                case 1 : return '<font color="orange">pause</font>';
                case 2 : return '<font color="green">running</font>';
                case 3 : return '<font color="red">error</font>';
                case 4 : return '<font color="blue">exit</font>';
              }
            }
        },{
            id: 'cmd',
            header: '命令',
            dataIndex: 'cmd',
            width: 70,
        },{
            id: 'info',
            header: '信息',
            dataIndex: 'info',
            width: 490,
            align: 'left',
        },{
            id: 'updated_time',
            header: '更新时间',
            dataIndex: 'updated_time',
            width: 150,
        },{
            id: 'edit',
            align: 'center',
            header: '操作',
            dataIndex: 'name',
            width: 180,
            renderer: function(value){
              var html = "<a href=\"javascript:void(0);\" onclick=\"javascript:send_cmd('" + value + "', 'PAUSE');\">pause</a>";
              html += "&nbsp;&nbsp;<a href=\"javascript:void(0);\" onclick=\"javascript:send_cmd('" + value + "', 'CONTINUE');\">continue</a>";
              html += "&nbsp;&nbsp;<a href=\"javascript:void(0);\" onclick=\"javascript:send_cmd('" + value + "', 'EXIT');\">exit</a>";
              html += "&nbsp;&nbsp;<a href=\"javascript:void(0);\" onclick=\"javascript:send_cmd('" + value + "', 'DELETE');\">delete</a>";
              return html;
            }
        }],
        tbar: [
          {
        	ref: '../addBtn',
            iconCls: 'task-add',
            text: '全部停止',
            handler: function(){
			    add_cup();
            }
        }],
    });
    var downurl_panel = new Ext.Panel({
        title: '引擎监控',
        layout: 'border',
        renderTo: 'data_div',
        layoutConfig: {
            columns: 1
        },
        height:650,
        items: [data_grid_panel]
    });
    
    store.load();
    
    send_cmd = function(name, cmd){
      
       Ext.Ajax.request({ 
			url : '/admin/engine_status_cmd/',
			sync : true,
			params : { 
			  cmd: cmd,
			  name: name,
			},
		    success : function(response) { 
		        Ext.Msg.show({ 
			      title : '请求成功', 
			      msg : '命令发送成功!', 
			      icon : Ext.Msg.SUCCESS, 
			      buttons : Ext.Msg.OK 
			    })
			    store.load();
			}, 
		    failure : function(response){ 
			    Ext.Msg.show({ 
			      title : '请求出错', 
			      msg : '请求出错!', 
			      icon : Ext.Msg.ERROR, 
			      buttons : Ext.Msg.OK 
			    })
			}
	    }) 
    };
    
});
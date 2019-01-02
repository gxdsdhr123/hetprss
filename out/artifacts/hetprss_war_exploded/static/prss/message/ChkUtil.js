function inputRead(){
    	var form = document.forms[0] ;
    	for( var i=0;i<form.elements.length;i++ ){
          var ele = form.elements[i] ;
          if( ele.type=="text" ){
            ele.className = "inputread" ;
            ele.setAttribute("readOnly",true); 
          }
          if(ele.type.indexOf("select")!=-1 ){
        	  ele.disabled=true; 
            }
          if(ele.type=="radio"){
  			ele.disabled = 'true';
  		  }
            if( ele.type=="checkbox" ){
  			ele.disabled = 'true';
  		  }
            if( ele.type=="textarea" ){
              ele.className = "inputread" ;
              ele.setAttribute("readOnly",true); 
            }
		}
    } 
// form item read functions
function ChkUtil(){
  //---------------
  // data Validity check
  //-----------------
  this.checkFormInput = function ( text,chknull,intype,length ){
    if( text == null || text == "" ){
		if( chknull || chknull=="true"){
		  return "数据不能为空" ;
		}else{
	          return "" ;
		}
    }
    //
    if( length >0 ){
      if( ! this.checkLength(text,length ) ){
	return "数据长度不能超过"+length+"字符" ;
      }
    }
    //
    if( intype  == "int" ){
      if( ! this.checkInteger(text) ){
	return "数据非整数格式" ;
      }
    }else if(intype == "float"){
      if( ! this.checkFloat(text) ){
	return "数据非浮点格式" ;
      }
    }else if(intype == "date"){
      if( ! this.checkDate(text) ){
	return "数据非日期格式[yyyy-MM-dd]" ;
      }
    }else if(intype == "time"){
      if( ! this.checkTime(text) ){
	return "数据非时间格式[hh:mm:ss]" ;
      }
    }else if(intype == "datetime"){
      if( ! this.checkDateTime(text) ){
	return "数据非时间格式[yyyy-MM-dd hh:mm:ss]" ;
      }
    }else if(intype == "postcode"){
      if( ! this.checkPostalcode(text) ){
	return "数据非邮编格式[6位数字]" ;
      }
    }else if(intype == "ipcode"){
      if( ! this.checkIPcode(text) ){
	return "数据非IP地址格式[0.0.0.0]" ;
      }
    }else if(intype == "email"){
      if( ! this.checkEmail(text) ){
	return "数据非邮箱地址格式[abc@domain.com]" ;
      }
     }else if(intype == "chinese"){
        if( ! this.checkChinese(text) ){
    return "数据非中文格式" ;
       }
    }else if(intype == "english"){
        if( ! this.checkEnglish(text) ){
    return "数据非英文格式或数字格式" ;
       }
    }else  if(intype == "up"){
        if( ! this.checkupEnglish(text) ){
      return "数据非大写英文" ;
          }
    }


    //
    return "" ;
  }
  this.checkLength = function(text,length){
    var tlen =  text.match(/[^ -~]/g)==null?text.length:text.length+text.match(/[^ -~]/g).length;
    return tlen <=length ;
  }
  this.checkValidity = function( re , text ){
    var arr;
    if ((arr = re.exec(text)) != null){
      if( arr == text ){
        return true ;
      }
    }
    return false ;
  }
  this.checkInteger = function( text ){
    var re = /[-]{0,1}[\d]+/g;
    return  this.checkValidity( re,text ) ;
  }
  this.checkFloat = function( text ){
    var re = /[-]{0,1}[\d]+[\.]{0,1}[\d]*/g;
    return  this.checkValidity( re,text ) ;
  }
  this.checkDate = function( text ){
    var re = /\d{4}-\d{1,2}-\d{1,2}/g;
    return  this.checkValidity( re,text ) ;
  }
  this.checkTime = function( text ){
    var re = /\d{1,2}:\d{1,2}:\d{1,2}/g;
    return  this.checkValidity( re,text ) ;
  }
  this.checkDateTime = function( text ){
    var re = /\d{4}-\d{1,2}-\d{1,2}\s\d{1,2}:\d{1,2}:\d{1,2}/g ;
    return  this.checkValidity( re,text ) ;
  }
  this.checkPostalcode = function( text ){
    var re = /[\d]{6}/g;
    return  this.checkValidity( re,text ) ;
  }
  this.checkIPcode = function( text ){
    var re = /\d{1,3}.\d{1,3}.\d{1,3}.\d{1,3}/g ;
    return  this.checkValidity( re,text ) ;
  }
  this.checkEmail = function( text ){
    var re = /^(?:\w+\.?)*\w+@(?:\w+\.?)*\w+$/g ;
    return  this.checkValidity( re,text ) ;
  }
  this.checkUpperEn = function( text ){
    var re = /^(?:\w+\.?)*\w+@(?:\w+\.?)*\w+$/g ;
    return  this.checkValidity( re,text ) ;
  }
  
  this.checkupEnglish = function( text ){
	    var re = /^[A-Z]+$/ ;
	    return  this.checkValidity( re,text ) ;
	}
  this.checkChinese = function( text ){
	  
	    var re = /^\W+$/ ;
	    return  this.checkValidity( re,text ) ;
	}
  this.checkEnglish = function( text ){
	    var re = /[^\u4e00-\u9fa5]+/ ;
	    return  this.checkValidity( re,text ) ;
	}

}




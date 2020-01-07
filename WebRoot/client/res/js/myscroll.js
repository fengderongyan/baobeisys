var myScroll,
	pullDownEl, pullDownOffset,
	pullUpEl, pullUpOffset;

var hasMoreData = false;
var upPageIndex = -1;
var pageIndex = 0;
var pageSize = 10;
var t1 = null;
	
//下拉加载数据方法，在自己的页面上重写	
function pullDownData(){
	
}

//上拉加载数据方法，在自己的页面上重写
function pullUpData(){
	
}

function createRow(index, req) {
	
}

function loadData(url, params, loadType){
	if(params != ''){
		params = params + '&pageSize='+pageSize+'&currenr_page='+pageIndex+'&currenr_page_up='+upPageIndex;
	} else {
		params = 'pageSize='+pageSize+'&currenr_page='+pageIndex+'&currenr_page_up='+upPageIndex;
	}
	new MyJqueryAjax(url, params, function(req){
		for (i=0; i<req.length; i++) {
			if(loadType == 'up'){
				$('#datalist').prepend(createRow(i, req[i]));
			} else {
				$('#datalist').append(createRow(i, req[i]));
			}
		}
		
		if(loadType != 'up'){//不是下拉刷新上部
			hasMoreData = true;
			if(req.length <= 0 || req.length < pageSize){
				$('#pullUp').hide();
				hasMoreData = false;
			} else {
				pageIndex++;
			}
			
			if(pageIndex == 0 && req.length == 0){
				$('#datalist').html('<div style="padding: 5px;color:red;font-weight: bold;margin-top:5px;">未查询到数据！<div>');
				hasMoreData = false;
			}
		}
		
		sgy.phone.closeProgress();
	}, 'JSON').request();
}

function requestCheck(){
	if (t1 == null){
        t1 = new Date().getTime();
    }else{       
        var t2 = new Date().getTime();
        if(t2 - t1 < 1000){
            t1 = t2;
            return false;
        }else{
            t1 = t2;
        }
    }
    return true;
}

function pullDownAction () {
	setTimeout(function () {
		pullDownData();
		
		myScroll.refresh();
	}, 1000);
}

function pullUpAction () {
	setTimeout(function () {
		pullUpData();
		
		myScroll.refresh();
	}, 1000);
}

function loaded() {
	pullDownEl = document.getElementById('pullDown');
	pullDownOffset = pullDownEl.offsetHeight;
	pullUpEl = document.getElementById('pullUp');	
	pullUpOffset = pullUpEl.offsetHeight;
	
	myScroll = new iScroll('wrapper', {
		useTransition: true,
		topOffset: pullDownOffset,
		onRefresh: function () {
			if (pullDownEl.className.match('loading')) {
				pullDownEl.className = '';
				pullDownEl.querySelector('.pullDownLabel').innerHTML = '下拉刷新数据...';
			} else if (pullUpEl.className.match('loading')) {
				pullUpEl.className = '';
				pullUpEl.querySelector('.pullUpLabel').innerHTML = '上拉加载更多...';
			}
		},
		onScrollMove: function () {
			if (this.y > 5 && !pullDownEl.className.match('flip')) {
				//console.log(this.y);
				pullDownEl.className = 'flip';
				pullDownEl.querySelector('.pullDownLabel').innerHTML = '松开刷新...';
				this.minScrollY = 0;
			} else if (this.y < 5 && pullDownEl.className.match('flip')) {
				pullDownEl.className = '';
				pullDownEl.querySelector('.pullDownLabel').innerHTML = '下拉刷新数据...';
				this.minScrollY = -pullDownOffset;
			} else if (this.y < (this.maxScrollY - pullUpOffset - 5) && !pullUpEl.className.match('flip')) {
				if (hasMoreData) {
					pullUpEl.className = 'flip';
					pullUpEl.querySelector('.pullUpLabel').innerHTML = '松开刷新...';
					this.maxScrollY = this.maxScrollY;
				}
			} else if (this.y > (this.maxScrollY - pullUpOffset - 5) && pullUpEl.className.match('flip')) {
				if (hasMoreData) {
					pullUpEl.className = '';
					pullUpEl.querySelector('.pullUpLabel').innerHTML = '上拉加载更多...';
					this.maxScrollY = pullUpOffset;
				}
			}
		},
		onScrollEnd: function () {
			if (pullDownEl.className.match('flip')) {
				pullDownEl.className = 'loading';
				pullDownEl.querySelector('.pullDownLabel').innerHTML = 'Loading...';
				pullDownAction();			
			} else if (pullUpEl.className.match('flip')) {
				pullUpEl.className = 'loading';
				pullUpEl.querySelector('.pullUpLabel').innerHTML = 'Loading...';				
				pullUpAction();
			}
		}
	});
	
	setTimeout(function () { document.getElementById('wrapper').style.left = '0'; }, 300);
}

document.addEventListener('touchmove', function (e) { e.preventDefault(); }, false);
document.addEventListener('DOMContentLoaded', function () { setTimeout(loaded, 200); }, false);
from django.conf.urls import url
from . import views

urlpatterns = [

    url(r'^$', views.log),
    url(r'^login$',views.login),     #登录
    url(r'^register$',views.register),#注册
    url(r'^reg$',views.reg),
    url(r'^kw$',views.kw),
    url(r'^up$', views.up),
    url(r'^setKeyword$',views.setKeyword),#关键字
    url(r'^kwlist$', views.kwlist),  # 关键字
    url(r'^search$',views.search), #搜索（按关键字）
    url(r'^follow$',views.followteacher), #关注
    url(r'^recommend$',views.recommend),#推荐
    url(r'^details$',views.details),#详情
    url(r'^info$',views.user_info),#详情
    url(r'^followlist$',views.folowlist),#详情
    url(r'^uploadimg$', views.photo),  # 详情
    url(r'^changepassword$', views.changepassword),  # 详情
    url(r'^send$', views.send_message),  # 详情
    url(r'^getmsglist$', views.message_list),  # 详情
    url(r'^getlast$', views.recive),  # 详情
    url(r'^make$', views.makefile),  # 详情
]
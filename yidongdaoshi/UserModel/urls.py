from django.conf.urls import url
from . import views

urlpatterns = [

    url(r'^$', views.log),
    url(r'^login$',views.login),
    url(r'^register$',views.register),

]
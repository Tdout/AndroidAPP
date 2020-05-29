import datetime
from _overlapped import NULL
from hashlib import new

from django.shortcuts import render

# Create your views here.


from django.http import HttpResponse, JsonResponse, HttpResponseRedirect
from django.shortcuts import render, redirect
import json
import os
from keras.backend import switch
from tensorflow import case

from . import models
from UserModel.models import Teacher, Student, teacherKeyword, studentKeyword
import requests


class ComplexEncoder(json.JSONEncoder):
    def default(self, obj):
        if isinstance(obj, datetime.datetime):
            return obj.strftime('%Y-%m-%d %H:%M:%S')
        elif isinstance(obj, datetime.date):
            return obj.strftime('%Y-%m-%d')
        else:
            return json.JSONEncoder.default(self, obj)


def register(request):
    if request.method == 'GET':
        return HttpResponse("error")
    if request.method == "POST":
        user_id = request.POST.get('user_id')
        password = request.POST.get('password')
        identify = request.POST.get('identify')
        name = request.POST.get('name')
        if identify == 1:
            user = models.Teacher.objects.get(user_id=user_id);
            if user != NULL:
                return HttpResponse("该账号已注册")
            else:
                user_teacher = Teacher(user_id=user_id, password=password,name=name,signature='',photo_url='')
                user_teacher.save()
                makefile(user_id)
                return HttpResponse("注册成功")
        if identify == 2:
            user = models.Student.objects.get(user_id=user_id);
            if user != NULL:
                return HttpResponse("该账号已注册")
            else:
                user_student = Student(user_id=user_id, password=password,name=name,signature='',photo_url='')
                user_student.save()
                makefile(user_id)
                return HttpResponse("注册成功")


def login(request):
    if request.method == 'GET':
        return HttpResponse("error")
    if request.method == "POST":
        user_id = request.POST.get('user_id')
        password = request.POST.get('password')
        identify = request.POST.get('identify')
        if identify == 1:
            teacher = Teacher.objects.get(user_id=user_id)
            return password == teacher.password
        if identify == 2:
            student = Student.objects.get(user_id=user_id)
            return password == student.password


def log(request):
    return render(request, 'login.html')


def makefile(user_id):
    path = "./static/" + user_id
    isExists = os.path.exists(path)
    if not isExists:
        os.mkdir(path)
        return True
    else:
        return False


def setKeyword(request):    #设置关键词
    if request.method == 'GET':
        return HttpResponse("error")
    if request.method == 'POST':
        identify = request.POST.get('identify')
        user_id = request.POST.get('user_id')
        keyword = request.POST.get('keyword')
        if identify == 1:  #老师身份
            user = Teacher.objects.get(user_id=user_id)
            tk = teacherKeyword(keyword=keyword,teacher=user)
            tk.save()
        if identify == 2:  #学生身份
            user = Student.objects.get(user_id=user_id)
            sk = studentKeyword(keyword=keyword,Student=user)
            sk.save()

def search(request):    #关键词搜索
    if request.method == 'GET':
        return HttpResponse("error")
    if request.method == 'POST':
        identify = request.POST.get('identify')
        keyword = request.POST.get('keyword')
        if identify == 1:  #老师搜学生
            List = studentKeyword.objects.filter(keyword__contains=keyword)  #模糊搜索
            data = []
            for var in List:
                data.append(var.student)
            return HttpResponse(json.dumps(data))  #返回搜到的学生列表json
        if identify == 2:  #学生搜老师
            List = teacherKeyword.objects.filter(keyword__contains=keyword)  #模糊搜索
            data = []
            for var in List:
                data.append(var.student)
            return HttpResponse(json.dumps(data))  #返回搜到的老师列表json


def details(request): #查看详情
    if request.method == 'GET':
        return HttpResponse("error")
    if request.method == 'POST':
        identify = request.POST.get('identify')
        user_id = request.POST.get('user_id')
        if identify == 1:
            stu = Student.objects.get(user_id=user_id)
            stukwList = studentKeyword.objects.filter(student=stu)
            detail = []
            detail.append(stu)
            detail.append(stukwList)
            return HttpResponse(json.dumps(detail))
        if identify == 2:
            tea = Teacher.objects.get(user_id=user_id)
            teakwList = teacherKeyword.objects.filter(teacher=tea)
            detail = []
            detail.append(tea)
            detail.append(teakwList)
            return HttpResponse(json.dumps(detail))

# def photo(request):
#     if request.method == 'GET':
#         return HttpResponse("error")
#     if request.method == 'POST':

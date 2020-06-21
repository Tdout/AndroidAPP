# -*- coding: utf-8 -*-
import datetime

from django.db.models import Q
from django.shortcuts import render

# Create your views here.


from django.http import HttpResponse, JsonResponse, HttpResponseRedirect
from django.shortcuts import render, redirect
import json
import os
from django.views.decorators.csrf import csrf_exempt
from numpy import byte

from . import models
from UserModel.models import *
from PIL import Image
import cv2
import base64
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
        body = request.body
        json_result = json.loads(body)

        name = json_result['name']
        user_id = json_result['user_id']
        password = json_result['password']
        identify = json_result['identity']
        school = json_result['school']

        try:
            user1 = models.Teacher.objects.get(user_id=user_id)
            user2 = models.Student.objects.get(user_id=user_id)

        except:
            if identify == 1:
                user_teacher = Teacher(user_id=user_id, password=password, name=name,
                                       school=school)
                user_teacher.save()
                makefile(user_id)
                return HttpResponse("注册成功")
            if identify == 2:
                user_student = Student(user_id=user_id, password=password, name=name,
                                       school=school)
                user_student.save()
                makefile(user_id)
                return HttpResponse("注册成功")
        else:
            return HttpResponse("已经注册")


def login(request):
    if request.method == 'GET':
        return HttpResponse("error")
    if request.method == "POST":
        body = request.body
        json_result = json.loads(body)
        user_id = json_result['user_id']
        password = json_result['password']
        identify = json_result['identity']
        # user_id = request.POST.get('user_id')
        # password = request.POST.get('password')
        # identify = request.POST.get('identify')
        if identify == 1:
            use = Teacher.objects.get(user_id=user_id)
        if identify == 2:
            use = Student.objects.get(user_id=user_id)
        if password == use.password:
            return HttpResponse(1)
        else:
            return HttpResponse(-1)


def log(request):
    return render(request, 'login.html')


def reg(request):
    return render(request, 'register.html')


def kw(request):
    return render(request, 'keyword.html')

def up(request):
    return  render(request,'upload.html')

def makefile(user_id):
    path = "./static/" + user_id
    isExists = os.path.exists(path)
    if not isExists:
        os.mkdir(path)
        f_src = open('./static/default/img.jpg','rb')
        content = f_src.read()
        f_copy = open(path+'/img.jpg','wb')
        f_copy.write(content)
        f_src.close()
        f_copy.close()
        return True
    else:
        return False


def setKeyword(request):  # 设置关键词
    if request.method == 'GET':
        return HttpResponse("error")
    if request.method == 'POST':
        body = request.body
        json_result = json.loads(body)
        kwtype = json_result['type']
        if kwtype == 1:
            user_id = json_result['user_id']
            keyword = json_result['keyword']
            identity = json_result['identity']
            if identity == 1:  # 老师身份
                user = Teacher.objects.get(user_id=user_id)
                tk = teacherKeyword(keyword=keyword, Teacher=user)
                tk.save()
                return HttpResponse("success")
            if identity == 2:  # 学生身份
                user = Student.objects.get(user_id=user_id)
                sk = studentKeyword(keyword=keyword, Student=user)
                sk.save()
                return HttpResponse("success")
        if kwtype == 2:
            user_id = json_result['user_id']
            keyword = json_result['keyword']
            identity = json_result['identity']
            if identity == 1:  # 老师身份
                user = Teacher.objects.get(user_id=user_id)
                tk = teacherKeyword.objects.get(keyword=keyword, Teacher=user)
                tk.delete()
                return HttpResponse("delete success")
            if identity == 2:  # 学生身份
                user = Student.objects.get(user_id=user_id)
                sk = studentKeyword.objects.get(keyword=keyword, Student=user)
                sk.delete()
                return HttpResponse("delete success")
        if kwtype == 3:
            user_id = json_result['user_id']
            identity = json_result['identity']
            if identity == 1:  # 老师身份
                user = Teacher.objects.get(user_id=user_id)
                tk = teacherKeyword.objects.filter(Teacher=user)
                for var in tk:
                    var.delete()
                return HttpResponse("delete all success")
            if identity == 2:  # 学生身份
                user = Student.objects.get(user_id=user_id)
                sk = studentKeyword.objects.filter( Student=user)
                for var in sk:
                    var.delete()
                return HttpResponse("delete all success")






def search(request):  # 关键词搜索

    if request.method == 'GET':
        return HttpResponse("error")
    if request.method == 'POST':
        body = request.body
        json_result = json.loads(body)

        identify = json_result['identity']
        keyword = json_result['keyword']
        type = json_result['type']
        # identify = request.POST.get('identity')
        # keyword = request.POST.get('keyword')
        if identify == 1:  # 老师搜学生
            data = []
            if type == 1:
                List = studentKeyword.objects.filter(keyword__icontains=keyword)  # 模糊搜索
                for var in List:
                    a = {
                        "id": var.student.user_id,
                        "name": var.student.name,
                        "major": var.student.major,
                        "class": var.student.grade,
                        "url": var.student.photo_url
                    }
                    data.append(a)
                jsdata = json.dumps(data)
                return HttpResponse(jsdata)  # 返回搜到的学生列表json
            elif type == 2:
                List = Student.objects.filter(name__icontains=keyword)
            else:
                List = Student.objects.filter(user_id__icontains=keyword)
            for var in List:
                a = {
                    "id": var.user_id,
                    "name": var.name,
                    "major": var.major,
                    "class": var.grade,
                    "url": var.photo_url
                }
                data.append(a)

            jsdata = json.dumps(data)
            return HttpResponse(jsdata)  # 返回搜到的学生列表json
        if identify == 2:  # 学生搜老师
            data = []
            if type == 1:
                List1 = teacherKeyword.objects.filter(keyword__icontains=keyword)  # 模糊搜索
                for var in List1:
                    a = {
                        "id": var.teacher.user_id,
                        "name": var.teacher.name,
                        "major": var.teacher.major,
                        "info": var.teacher.experience,
                        "url": var.teacher.photo_url
                    }
                    data.append(a)
                jsdata = json.dumps(data)
                return HttpResponse(jsdata)  # 返回搜到的老师列表json
            elif type == 2:
                List1 = Teacher.objects.filter(name__icontains=keyword)
            else:
                List1 = Teacher.objects.filter(user_id__icontains=keyword)
            for var in List1:
                a = {
                    "id": var.user_id,
                    "name": var.name,
                    "major": var.major,
                    "info": var.experience,
                    "url": var.photo_url
                }
                data.append(a)

            jsdata = json.dumps(data)
            return HttpResponse(jsdata)  # 返回搜到的学生列表json


def details(request):  # 查看详情
    if request.method == 'GET':
        return HttpResponse("error")
    if request.method == 'POST':
        body = request.body
        json_result = json.loads(body)
        identify = json_result['identity']
        user_id = json_result['user_id']
        aim_id = json_result['aim_id']
        if identify == 1:
            stu = Student.objects.get(user_id=aim_id)
            tea = Teacher.objects.get(user_id=user_id)
            stukwList = studentKeyword.objects.filter(Student=stu)
            s = []
            try:
                fw = follow.objects.get(student=stu,teacher=tea)
                isfollow = 1
            except:
                isfollow = -1
            for var in stukwList:
                s.append(var.keyword)
            detail = {
                'name': stu.name,
                'signature': stu.signature,
                'info': stu.experience,
                'major': stu.major,
                'keyword': s,
                'isfollow': isfollow,
                'url':stu.photo_url,

            }
            return HttpResponse(json.dumps(detail))
        if identify == 2:
            tea = Teacher.objects.get(user_id=aim_id)
            stu = Student.objects.get(user_id=user_id)
            teakwList = teacherKeyword.objects.filter(Teacher=tea)
            s = []
            try:
                fw = follow.objects.get(student=stu,teacher=tea)
                isfollow = 1
            except:
                isfollow = -1
            for var in teakwList:
                s.append(var.keyword)
            detail = {
                'name': tea.name,
                'info': tea.experience,
                'signature': tea.signature,
                'major': tea.major,
                'keyword': s,
                'isfollow': isfollow
            }
            return HttpResponse(json.dumps(detail))


def followteacher(request):
    if request.method == 'GET':
        return HttpResponse("error")
    if request.method == 'POST':
        body = request.body
        json_result = json.loads(body)
        teacher_id = json_result['teacher_id']
        user_id = json_result['user_id']
        teacher = Teacher.objects.get(user_id=teacher_id)
        student = Student.objects.get(user_id=user_id)
        try:
            is_followed = follow.objects.get(teacher=teacher, student=student, relationship=1)
        except:
            fw = follow(teacher=teacher, student=student, relationship=1)
            fw.save()
            a={
                "isfollow":1
            }
            return JsonResponse(a)
        else:
            is_followed.delete()
            a = {
                "isfollow": -1
            }
            return JsonResponse(a)


@csrf_exempt
def recommend(request):
    if request.method == 'GET':
        return HttpResponse("error")
    if request.method == 'POST':
        body = request.body
        json_result = json.loads(body)
        # user_id = request.POST.get('user_id')
        user_id = json_result['user_id']
        identity = json_result['identity']
        if identity == 2:
            user = Student.objects.get(user_id=user_id)
            stkw = studentKeyword.objects.filter(Student=user)
            st = []
            for var in stkw:
                st.append(var.keyword)
            teacher = []
            tc = Teacher.objects.all()
            tckwlist = []
            for var in tc:
                tckw = teacherKeyword.objects.filter(Teacher=var)
                for key in tckw:
                    tckwlist.append(key.keyword)
                point = len(set(st) & set(tckwlist))
                a = {
                    "point":point,
                    "id": var.user_id,
                    "name": var.name,
                    "major": var.major,
                    "info": var.experience,
                    "url": var.photo_url
                }
                teacher.append(a)
            resultList = sorted(teacher, key=lambda x: x['point'], reverse=True)
            jsdata = json.dumps(resultList)
            return HttpResponse(jsdata)
        if identity == 1:
            user = Teacher.objects.get(user_id=user_id)
            tekw = teacherKeyword.objects.filter(Teacher=user)
            te = []
            for var in tekw:
                te.append(var.keyword)
            student = []
            st = Student.objects.all()
            stkwlist = []
            for var in st:
                stkw = studentKeyword.objects.filter(Student=var)
                for key in stkw:
                    stkwlist.append(key.keyword)
                point = len(set(te) & set(stkwlist))
                a = {
                    "point": point,
                    "id": var.user_id,
                    "name": var.name,
                    "major": var.major,
                    "info": var.experience,
                    "url": var.photo_url
                }
                student.append(a)
            resultList = sorted(student, key=lambda x: x['point'], reverse=True)
            jsdata = json.dumps(resultList)
            return HttpResponse(jsdata)


def user_info(request):
    if request.method == 'GET':
        return HttpResponse("error")
    if request.method == 'POST':
        body = request.body
        json_result = json.loads(body)
        user_id = json_result['user_id']
        identity = json_result['identity']
        user = object
        if identity == 1:
            user = Teacher.objects.get(user_id=user_id)
        elif identity == 2:
            user = Student.objects.get(user_id=user_id)
        a={
            'name':user.name,
            'old':user.old,
            'sex':user.sex,
            'email':user.email,
            'skill':user.skill,
            'grade':user.grade,
            'major':user.major,
            'experience':user.experience,
        }
        js = json.dumps(a)
        return HttpResponse(js)


def folowlist(request):
    if request.method == 'GET':
        return HttpResponse("error")
    if request.method == 'POST':
        body = request.body
        json_result = json.loads(body)
        user_id = json_result['user_id']
        identity = json_result['identity']
        ls = []
        if identity == 1:
            user = Teacher.objects.get(user_id=user_id)
            List = follow.objects.filter(teacher=user)
            for var in List:
                a = {
                    "id": var.student.user_id,
                    "name": var.student.name,
                    "major": var.student.major,
                    "info": var.student.experience,
                    "url": var.student.photo_url,
                    "class": var.student.grade
                }
                ls.append(a)
        elif identity == 2:
            user = Student.objects.get(user_id=user_id)
            List = follow.objects.filter(student=user)
            for var in List:
                a = {
                    "id": var.teacher.user_id,
                    "name": var.teacher.name,
                    "major": var.teacher.major,
                    "info": var.teacher.experience,
                    "url": var.teacher.photo_url
                }
                ls.append(a)
        js = json.dumps(ls)
        return HttpResponse(js)


def kwlist(request):
    if request.method == 'GET':
        return HttpResponse("error")
    if request.method == 'POST':
        body = request.body
        json_result = json.loads(body)
        user_id = json_result['user_id']
        identity = json_result['identity']
        ls = []
        if identity ==1:
            tc = Teacher.objects.get(user_id=user_id)
            tclist = teacherKeyword.objects.filter(Teacher=tc)
            for var in tclist:
                ls.append(var.keyword)
        if identity ==2:
            st = Student.objects.get(user_id=user_id)
            tclist = studentKeyword.objects.filter(Student=st)
            for var in tclist:
                ls.append(var.keyword)
        return HttpResponse(ls)

def photo(request):
    if request.method == 'GET':
        return HttpResponse("error")
    if request.method == 'POST':
        body = request.body
        json_result = json.loads(body)
        user_id = json_result['user_id']
        identity = json_result['identity']
        img = json_result['img']
        bs = base64.b64decode(img)
        url = './static/'+user_id+'/img.jpg'
        file = open(url, 'wb')
        file.write(bs)
        file.close()
        return HttpResponse("success")



def changepassword(request):
    if request.method == 'GET':
        return HttpResponse("error")
    if request.method == 'POST':
        body = request.body
        json_result = json.loads(body)
        user_id = json_result['user_id']
        identity = json_result['identity']
        old_password = json_result['old_password']
        new_password = json_result['new_password']
        if identity == 1:
            tc = Teacher.objects.get(user_id=user_id)
            ps = tc.password
            if old_password == ps:
                tc.password = new_password;
                tc.save()
                return HttpResponse(1)
            else:
                return HttpResponse(-1)
        if identity == 2:
            st = Student.objects.get(user_id=user_id)
            ps = st.password
            if old_password == ps:
                st.password = new_password
                st.save()
                return HttpResponse(1)
            else:
                return HttpResponse(-1)


def send_message(request):
    if request.method == 'POST':
        body = request.body
        json_result = json.loads(body)
        sender = json_result['user_id']
        reciver = json_result['aim_id']
        content = json_result['content']
        try:
            mes = message(send_user=sender,recive_user=reciver,content=content).save()
            return HttpResponse("send message")
        except:
            return HttpResponse("send error")

def message_list(request):
    if request.method == 'POST':
        body = request.body
        json_result = json.loads(body)
        user_id = json_result['user_id']
        aim_id = json_result['aim_id']

        msg_List = message.objects.filter( Q(send_user=user_id,recive_user=aim_id) |
                                           Q(send_user=aim_id,recive_user = user_id) )
        data = []
        for msg in msg_List:
            a={
                "time":datetime.datetime.strftime(msg.data_time,'%Y-%m-%d %H:%M:%S'),
                "sender":msg.send_user,
                "content":msg.content
            }
            data.append(a)
        js = json.dumps(data)
        return HttpResponse(js)

def recive(request):
    if request.method == 'POST':
        body = request.body
        json_result = json.loads(body)
        user_id = json_result['user_id']
        aim_id = json_result['aim_id']
        msg_List = message.objects.filter( Q(send_user=user_id,recive_user=aim_id) |
                                           Q(send_user=aim_id,recive_user = user_id) ).reverse()
        data = []
        try:
            a = {
                "time": datetime.datetime.strftime(msg_List[0].data_time, '%Y-%m-%d %H:%M:%S'),
                "sender": msg_List[0].send_user,
                "content": msg_List[0].content
            }
            data.append(a)
            return HttpResponse(data)
        except:
            return HttpResponse(data)

def changeinfo(request):
    if request.method == 'GET':
        return HttpResponse("error")
    if request.method == "POST":
        body = request.body
        json_result = json.loads(body)
        user_id = json_result['user_id']
        identity = json_result['identity']

        name = json_result['name']
        sex = json_result['sex']
        old = json_result['old']
        email = json_result['email']
        grade = json_result['grade']
        major = json_result['major']
        skill = json_result['skill']
        experience = json_result['experience']
        try:
            if identity == 1:
                user = Teacher.objects.get(user_id=user_id)
            if identity == 2:
                user = Student.objects.get(user_id=user_id)
        except:
            return HttpResponse(-1)
        else:
            user.name = name
            user.sex = sex
            user.old = old
            user.email = email
            user.grade = grade
            user.major = major
            user.skill = skill
            user.experience = experience
            user.save()
            return HttpResponse(1)



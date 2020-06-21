
from django.db import models


# Create your models here.


class Teacher(models.Model):
    user_id = models.CharField(max_length=15)
    password = models.CharField(max_length=15)
    name = models.CharField(max_length=128)
    school = models.CharField(max_length=128)
    old = models.IntegerField(default=0)
    grade = models.CharField(max_length=10,default="")
    sex = models.CharField(max_length=10,default="")
    experience = models.CharField(max_length=256,default="")
    major = models.CharField(max_length=256, default="")
    email = models.CharField(max_length=128,default="")
    skill = models.TextField(default="")
    photo_url = models.TextField(default="")



class Student(models.Model):
    user_id = models.CharField(max_length=15)
    password = models.CharField(max_length=15)
    name = models.CharField(max_length=128)
    old = models.IntegerField(default=0)
    sex = models.CharField(max_length=10,default="")
    school = models.CharField(max_length=128)
    grade = models.CharField(max_length=128,default="")
    major = models.CharField(max_length=128,default="")
    email = models.CharField(max_length=128,default="")
    experience = models.CharField(max_length=256, default="")
    skill = models.TextField(default="")
    photo_url = models.TextField(default="")


    teachers = models.ManyToManyField(Teacher, through='follow')


class follow(models.Model):
    student = models.ForeignKey(Student, on_delete=models.CASCADE)
    teacher = models.ForeignKey(Teacher, on_delete=models.CASCADE)
    relationship = models.IntegerField()

    class Meta:
        db_table = "student_teacher_follow"


class teacherKeyword(models.Model):
    keyword = models.CharField(max_length=20)
    Teacher = models.ForeignKey(Teacher, on_delete=models.CASCADE)


class studentKeyword(models.Model):
    keyword = models.CharField(max_length=20)
    Student = models.ForeignKey(Student, on_delete=models.CASCADE)


class message(models.Model):
    content = models.TextField(max_length=256,default="")
    send_user = models.CharField(max_length=128,default="")
    recive_user = models.CharField(max_length=128,default="")
    data_time = models.DateTimeField(auto_now_add=True)

    class Meta:
        db_table = "message"
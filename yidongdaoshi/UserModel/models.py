from django.db import models


# Create your models here.


class Teacher(models.Model):
    user_id = models.CharField(max_length=15)
    password = models.CharField(max_length=15)
    name = models.CharField(max_length=128)
    signature = models.TextField(max_length=128,null=True)
    photo_url = models.TextField(null=True)



class Student(models.Model):
    user_id = models.IntegerField(max_length=15)
    password = models.CharField(max_length=15)
    name = models.CharField(max_length=128)
    signature = models.TextField(max_length=128,null=True)
    photo_url = models.TextField(null=True)

    teachers = models.ManyToManyField(Teacher, through='follow')


class follow(models.Model):
    student = models.ForeignKey(Student, on_delete=models.CASCADE)
    teacher = models.ForeignKey(Teacher, on_delete=models.CASCADE)
    relationship = models.IntegerField()

    class Meta:
        db_table = "student_teacher_follow"


class teacherKeyword(models.Model):
    keyword = models.CharField(max_length=20)
    teacher = models.ForeignKey(Teacher, on_delete=models.CASCADE)


class studentKeyword(models.Model):
    keyword = models.CharField(max_length=20)
    student = models.ForeignKey(Student, on_delete=models.CASCADE)

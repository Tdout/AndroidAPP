# Generated by Django 2.2.5 on 2020-06-20 20:08

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('UserModel', '0005_auto_20200619_1404'),
    ]

    operations = [
        migrations.AddField(
            model_name='teacher',
            name='grade',
            field=models.CharField(default='', max_length=10),
        ),
        migrations.AlterField(
            model_name='student',
            name='photo_url',
            field=models.TextField(null=True),
        ),
        migrations.AlterField(
            model_name='teacher',
            name='photo_url',
            field=models.TextField(null=True),
        ),
    ]

import base64
import json
from django.contrib import auth
from django.contrib.auth.models import User
from django.http import HttpResponse
from django.utils import timezone
from django.shortcuts import render, get_object_or_404, redirect
import socket
import os
from django.views.decorators.csrf import csrf_exempt

@csrf_exempt
def mytry(request):
    u_entry = "MyUserEntry.txt"
    if os.path.exists(u_entry):
        f = open(u_entry, 'r')
        i = f.read()
        sym = i[:3]
        f.close()
        o = open(u_entry, 'w')
        o.write("non")
        o.close()
        return HttpResponse(sym)
    return HttpResponse("non")

@csrf_exempt
def accepted_reg(request):
    f = open('acc.txt', 'w')
    f.write('reg_ok')
    f.close()
    return HttpResponse("reg_ok")

@csrf_exempt
def accepted(request):
    f = open('acc.txt', 'w')
    f.write('ok')
    f.close()
    return HttpResponse("ok")

@csrf_exempt
def check_acc(request):
    fname = "acc.txt"
    if os.path.exists(fname):
        f = open(fname, 'r')
        data = f.read()
        f.close()
        f = open(fname, 'w')
        f.write('fail')
        f.close()
        return HttpResponse(data)
    return HttpResponse("fail")

def auth(request):	#TODO: finish
    text = "preceq"
    if text is not None:
        f = open( 'some_file.txt', 'w+')
        f.write(text)
        f.close()
    u_entry = "MyUserEntry.txt"
    o = open(u_entry, 'w')
    o.write("non")
    o.close()
    return render(request, "django_project/auth.html", {})

def send(request):
    if request.method == "POST":
        status = ""
        u_entry = "MyUserEntry.txt"
        if "reg" in request.POST:
            o = open(u_entry, 'w')
            o.write("reg")
            o.close()
            status = "Registring ..."
        elif "log" in request.POST:
            o = open(u_entry, 'w')
            o.write("log")
            o.close()
            status = "Logging on ..."

    #    TCP_IP = '192.168.43.3'
    #    TCP_PORT = 5005
    #    BUFFER_SIZE = 1024
    #    MESSAGE = "Hello, World!"

    #    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    #    s.connect((TCP_IP, TCP_PORT))
    #    s.send(MESSAGE)
    #data = s.recv(BUFFER_SIZE)
    #    s.close()
 
    return render(request, "django_project/auth2.html", {})

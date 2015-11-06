# -*- coding: utf-8 -*-
import os
import stat

def walk(top, topdown=True, onerror=None):
    try:
        names = os.listdir(top)
    except os.error, err:
        if onerror is not None:
            onerror(err)
        return

    dirs, nondirs = [], []
    for name in names:
        if os.path.isdir(os.path.join(top, name)):
            dirs.append(name)
        else:
            nondirs.append(name)
    
    dirs.sort()
    nondirs.sort()
    if topdown:
        yield top, dirs, nondirs
    for name in dirs:
        path = os.path.join(top, name)
        if not os.path.islink(path):
            for x in walk(path, topdown, onerror):
                yield x
    if not topdown:
        yield top, dirs, nondirs
        
def get_files(dir):
    """获取一个文件夹下的所有文件"""
    if not dir or not os.path.isdir(dir):
        return []
    names = os.listdir(dir)
    files = []
    for name in names:
        name = os.path.join(dir, name)
        if os.path.isfile(name):
            files.append(name)
    return files

def get_tree_files(dir):
    if dir and os.path.isdir(dir):
        walker = os.walk(dir)
        for item in walker:
            for fileName in item[2]:
                filePath = os.path.join(item[0], fileName)
                if os.path.isfile(filePath):
                    yield filePath

def get_dirs(dir):
    """获取一个文件夹下的所有文件"""
    if not dir or not os.path.isdir(dir):
        return []
    names = os.listdir(dir)
    files = []
    for name in names:
        name = os.path.join(dir, name)
        if os.path.isdir(name):
            files.append(name)
    return files

def get_tree_dirs(dir):
    if dir and os.path.isdir(dir):
        walker = os.walk(dir)
        for item in walker:
            for fileName in item[1]:
                filePath = os.path.join(item[0], fileName)
                if os.path.isdir(filePath):
                    yield filePath

def ensure_dir_exists(dirPath):
    if not os.path.exists(dirPath):
        try:
            os.makedirs(dirPath)
        except OSError, e:
            if not e.args[0] in (183, 17):
                raise

def is_empty_dir(dir):
    files = get_tree_files(dir)
    try:
        files.next()
        return False
    except StopIteration:
        return True
    finally:
        files.close()
        
def _default_error_handler(e):
    raise

def remove_dir_tree(dir, on_error = _default_error_handler):
    if dir and os.path.isdir(dir):
        walker = os.walk(dir, False, onerror = on_error)
        for item in walker:
            for d in item[1]:
                p = os.path.join(item[0], d)
                try:
                    os.rmdir(p)
                except Exception, e:
                    on_error(e)
            for f in item[2]:
                p = os.path.join(item[0], f)
                try:
                    set_file_writable(p)
                    os.remove(p)
                except Exception, e:
                    on_error(e)
        try:
            os.rmdir(dir)
        except Exception, e:
            on_error(e)

def remove_empty_dir(dir, includeSelf = False):
    if not dir: return
    if not os.path.isdir(dir): return
    exceptions = ('.', '..')
    isEmpty = True
    names = os.listdir(dir)
    for name in names:
        if name not in exceptions:
            name = os.path.join(dir, name)
            if os.path.isfile(name):
                isEmpty = False
            elif not remove_empty_dir(name, True):
                isEmpty = False
    if includeSelf and isEmpty:
        remove_dir_tree(dir)
        
def chmod(filePath, privilege):
    if os.path.exists(filePath):
        info = os.stat(filePath)
        if info.st_mode & privilege != privilege:
            os.chmod(filePath, info.st_mode | privilege)

def chmod_777(filepath):
    os.chmod(filepath, 00777)

def set_file_writable(filePath):
    chmod(filePath, stat.S_IWRITE)
    
def create_hard_link(src, dest):
    if hasattr(os, "link"):
        os.link(src, dest)
    else:
        import win32file
        win32file.CreateHardLink(dest, src)

def link_files(src_folder, dest_folder, overwrite = False):
    if not src_folder or not dest_folder:
        return
    walker = os.walk(src_folder)
    src_folder_len = len(src_folder)
    for top, folders, files in walker:
        if not files:
            continue
        del folders
        folder_part = top[src_folder_len:]
        if folder_part[0:1] in ("/", "\\"):
            folder_part = folder_part[1:]
        to_folder = os.path.join(dest_folder, folder_part)
        ensure_dir_exists(to_folder)
        for file_name in files:
            file_path = os.path.join(top, file_name)
            dest_path = os.path.join(to_folder, file_name)
            if overwrite and os.path.exists(dest_path):
                os.remove(dest_path)
            if not os.path.exists(dest_path):
                create_hard_link(file_path, dest_path)

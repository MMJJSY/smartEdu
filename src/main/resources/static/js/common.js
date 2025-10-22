function OpenWindow(UrlStr, WinTitle, WinWidth, WinHeight) {
    var winleft = (screen.width - WinWidth) / 2;
    var wintop = (screen.height - WinHeight) / 2;
    var win = window.open(
        UrlStr,
        WinTitle,
        "scrollbars=yes,width=" + WinWidth +
        ",height=" + WinHeight +
        ",top=" + wintop +
        ",left=" + winleft +
        ",resizable=yes,status=yes");
    win.focus();
}

function CloseWindow() {
    window.opener.location.reload(true);
    window.close();
}

var contextPath = "";
function summernote_go(target, context) {
    contextPath = context;

    $(target).summernote({
        placeholder: '여기에 내용을 적으세요.',
        lang: 'ko-KR',
        height: 250,
        disableResizeEditor: true,
        callbacks: {
            onImageUpload: function (files, editor, welEditable) {
                for (let file of files) {
                    //alert(file.name);
                    if (!["JPG", "JPEG", "PNG", "GIF", "WEBP"].includes(
                        file.name.substring(file.name.lastIndexOf(".") + 1).toUpperCase()
                    )) {
                        alert("이미지 형식은 JPG, JPEG, PNG, GIF, WEBP만 가능합니다.");
                        return;
                    }
                    if (file.size > 1024 * 1024 * 1) {
                        alert("이미지는 1MB 미만입니다.");
                        return;
                    }
                }

                for (var file of files) {
                    sendFile(file, this);
                }
            },
            onMediaDelete: function (target) {
                // alert(target[0].src.split("=")[1]);
                let fileName = target[0].src.split("=")[1];
                deleteFile(fileName);
            }
        }

    });
}

function sendFile(file, el) {
    var form_data = new FormData();
    form_data.append("file", file);

    $.ajax({
        url: contextPath + "summernote/uploadImg",
        method: "post",
        data: form_data,
        contentType: false,
        processData: false,
        success: function (img_url) {
            //    alert(img_url);
            $(el).summernote('editor.insertImage', img_url);
        },
        error: function (error) {
            alert("서버 장애로 이미지 추가가 불가합니다.");
        }
    });
}

function deleteFile(fileName) {
    $.ajax({
        url: contextPath + "summernote/deleteImg?fileName=" + fileName,
        success: function (res) {
            console.log(res);
        },
        error: function (error) {
            alert("서버장애로 이미지 삭제 실패");
        }
    });
}

function CoursePictureThumb(contextPath) {
    for (let target of document.querySelectorAll('.manPicture')) {
        let id = target.getAttribute('data-id');
        if (id) {
            target.style.backgroundImage = `url('${contextPath}course/getImg?id=${id}')`;
            target.style.backgroundRepeat = "no-repeat";
            target.style.backgroundSize = "cover";
            target.style.backgroundPosition = "center";
        }


    }
}
console.log("boardComment.js in");
console.log(bnoVal);

document.getElementById('cmtAddBtn').addEventListener('click',()=>{
    let cmtText = document.getElementById('cmtText');
    if(cmtText.value == null || cmtText.value == ''){
        alert('댓글을 입력해주세요');
        cmtText.focus();
        return false;
    }else{
        //댓글 등록
        let cmtData = {
            bno: bnoVal,
            writer: document.getElementById('cmtWriter').innerText,
            content: cmtText.value
        };
        console.log(cmtData);
        //댓글 비동기로 전성
        postCommentToServer(cmtData).then(result=>{
            if(result == '1'){
                alert('댓글 등록 성공!!');
                cmtText.value="";
                //화면에 뿌리기
            }
            spreadCommentList(bnoVal);
        })
    }
});

//비동기 통신 resultful
//post : 데이터 객체를 컨트롤러로 보낼때 (삽입)
//get : 조회 (생략가능)
//put(pathc) : 수정
//delete : 삭제
async function postCommentToServer(cmtData){
    try{
        const url = "/comment/post";
        const config = {
            method:"post",
            headers:{
                "content-type":"application/json; charset=utf-8"
            },
            body:JSON.stringify(cmtData)
        };
        const resp = await fetch(url, config);
        const result = await resp.text(); // isOk
        return result;
    } catch(error){
        console.log(error);
    }
}

//페이징 처리를 하여, 한페이지(더보기)에 5개 댓글을 출력
//전체 게시글 수에 따른 페이지 수
function spreadCommentList(bno, page=1){ //result = 댓글 리스트
    getCommentListFromServer(bno, page).then(result =>{
        console.log(result);
        let ul = document.getElementById('cmtlistArea');
        //ul.innerHTML="";
        if(result.cmtList.length > 0){
            if(page==1){
                ul.innerHTML = '';
            }
            for(let cvo of result.cmtList){
                let li = `<li class="list-group-item" data-cno="${cvo.cno}">`;
                li += `<div class="mb-3">no. ${cvo.cno}`;
                li += `<div class="fw-bold">${cvo.writer}</div>`;
                li += `${cvo.content}`;
                li += `</div>`;
                li += `<span class="badge text-bg-warning">${cvo.regAt}</span>`;
                // 수정 삭제 버튼
                li += `<button type="button" class="btn btn-outline-warning btn-sm mod" data-bs-toggle="modal" data-bs-target="#myModal">수정</button>`;
                li += `<button type="button" data-cno="${cvo.cno}" class="btn btn-outline-danger btn-sm del">삭제</button>`;
                li += `</li>`;
                ul.innerHTML += li;
            }
            //더보기 버튼 코드
            let moreBtn = document.getElementById('moreBtn');
            console.log(moreBtn);
            //moreBtn 표시되는 조건
            // pgvo.pageNo = 1 / realEndPage = 3
            //realEndPage보다 현재 내 페이지가 작으면 표시
            if(result.pgvo.pageNo < result.endPage){
               // style="visibility:hidden"=숨김 "visibility:visible" = 표시
               moreBtn.style.visibility = 'visible' //버튼 표시
               moreBtn.dataset.page = page+1; //페이지 1 늘림
            }else{
                moreBtn.style.visibility ='hidden'; //숨김
            }

        }else{
            let li=`<li class="list-group-item">Comment List Empty</li>`;
            ul.innerHTML=li;
        }
    })
}


async function getCommentListFromServer(bno, page){
    try {
        // /comment/306
        const resp = await fetch("/comment/list/"+bno+"/"+page);
        const result = await resp.json();
        return result;
    } catch (error) {
        console.log(error);
    }
    
}

async function updateCommentToServer(cmtModData){
    try {
        const url = "/comment/modify";
        const config ={
            method:"put" ,
            headers:{
                'content-type':'application/json; charset=utf-8'
            },
            body: JSON.stringify(cmtModData)
        };

        const resp = await fetch(url, config);
        const result = resp.text();
        return result;
    } catch (error) {
        console.log(error);
    }
}

async function removeCommentFromServer(cno){
    try {
        const url = "/comment/remove/"+cno;
        const config ={
            method: "delete"
        };
        const resp = await fetch(url, config);
        const result = await resp.text();
        return result;
    } catch (error) {
        console.log(error);
    }
}

document.addEventListener('click',(e)=>{
    if(e.target.id == 'moreBtn'){
        let page = parseInt(e.target.dataset.page);
        spreadCommentList(bnoVal, page);
    }else if(e.target.classList.contains('mod')){
        //내가 수정버튼을 누른 댓글의 li
        let li = e.target.closest('li');
        //writer 를 찾아서 id="modwriter" 에 넣기
        let modWriter = li.querySelector('.fw-bold').innerText;
        document.getElementById('modWriter').innerText=modWriter;

        //nextSibling : 한 부모 안에서 다음 형제를 찾기
        let cmtText = li.querySelector('.fw-bold').nextSibling;
        console.log(cmtText);
        document.getElementById('cmtTextMod').value = cmtText.nodeValue;

        //수정 => cno dataset으로 달기 cno, content
        document.getElementById('cmtModBtn').setAttribute("data-cno", li.dataset.cno);
    }
    else if(e.target.id == 'cmtModBtn'){
        let cmtModData = {
            cno: e.target.dataset.cno,
            content: document.getElementById('cmtTextMod').value
        };
        console.log(cmtModData);

        //비동기로 보내기
        updateCommentToServer(cmtModData).then(result =>{
            if(result == '1'){
                alert('댓글 수정 성공');
                spreadCommentList(bnoVal);
            }
        })
    }
    else if(e.target.classList.contains('del')){
        let li = e.target.closest('li');
        let cnoVal = li.dataset.cno;
        removeCommentFromServer(cnoVal).then(result =>{
            if(result == '1'){
                alert('댓글 삭제 성공');
                spreadCommentList(bnoVal);
            }
        })
    }
})
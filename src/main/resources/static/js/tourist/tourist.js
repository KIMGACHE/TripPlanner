
// areaCode 값에 따라 키를 매칭시켜 어느 지역인지를 알려줄 변수
const regionMap = {
  "1": "서울",
  "2": "인천",
  "3": "대전",
  "4": "대구",
  "5": "광주",
  "6": "부산",
  "7": "울산",
  "8": "세종",
  "31": "경기",
  "32": "강원",
  "33": "충북",
  "34": "충남",
  "35": "경북",
  "36": "경남",
  "37": "전북",
  "38": "전남",
  "39": "제주"
};

// 전역 변수
let currentEncodedData;
let currentPage = 1;
let currentRegion = '';
let currentHashtag = '';
// 관광타입 (12:관광지, 14:문화시설, 15:축제공연행사, 25:여행코스, 28:레포츠, 32:숙박, 38:쇼핑, 39:음식점) ID
let contentTypeId = 25;


const courseContent = document.querySelector('.course_content_wrap');
const regionFilter = document.getElementById('regionFilter');
const hashtagFilter = document.getElementById('hashtagFilter');
const filterButton = document.querySelector('.course_formButton');
const buttonBox = document.querySelector('.buttonBox');
const searchFilter = document.getElementById('searchFilter');

// 검색어를 URL 인코딩하는 함수
function encodeUTF8() {
    currentEncodedData = encodeURIComponent(searchFilter.value);
}



// 검색어에 맞춰 데이터를 가져오는 함수
async function getSearchKeyword() {

    try {
        const response = await axios.get('/api/getSearchKeyword', {
            params : {
                keyword: currentEncodedData,
                pageNo: currentPage,
                hashtag: currentHashtag,
                regionCode : currentRegion
            }
        });
        console.log(response);
        return response;
    } catch (error) {
        console.log('getSearchKeyword의 error : ', error);
    }
}


// 지역 및 해시태그에 맞춰 데이터를 가져오는 함수
async function getAreaBasedList() {
    try {
        const response = await axios.get('/api/getAreaBasedList', {
            params : {
                regionCode: currentRegion,
                hashtag: currentHashtag,
                pageNo: currentPage
            }
        });
        console.log(response);
        return response;
    } catch (error) {
        console.log('getAreaBasedList의 error : ', error);
    }

}


//// 상세 정보를 표시할 axios요청
//async function getDetailCommon() {
//
//    try {
//        const response = await axios.get('/api/getSearchKeyword', {
//            params : {
//                keyword: currentEncodedData,
//                pageNo: currentPage
//            }
//        });
//        console.log(response);
//        return response;
//    } catch (error) {
//        console.log('getSearchKeyword의 error : ', error);
//    }
//
//}

// 검색을 할 때 만약 지역이나, 태그를 선택했을 때 실행할 함수
// 제공 API에 키워드, 지역, 태그를 전부 포함하는 요청이 없어서 직접 구현
async function getSearchFilter() {
    try {
        const response = await axios.get('/api/searchFilter', {
            params : {
                regionCode: currentRegion,
                hashtag: currentHashtag,
                pageNo: currentPage,
                keyword: currentEncodedData
            }
        });
        console.log(response);
        return response;
    } catch(error) {
        console.log(error);
    }

}


// 요소들 클릭 시 ContentId를 받아서 상세 정보를 가져오는 함수
async function getDetailInfo(contentId) {

    try {
        const response = await axios.get('/api/getDetailInfo', {
            params : {
                contentId: contentId,
                pageNo: currentPage
            }
        });
        console.log(response);
        return response;

    } catch (error) {
        console.log('getDetailInfo의 error : ', error);
    }

}






// 코스 데이터를 HTML로 변환하여 페이지에 삽입하는 함수
async function renderCourseData(jsonData) {
    courseData = jsonData.data.response.body.items.item;
    totalCount = jsonData.data.response.body.totalCount;
    console.log('totalCount : ', totalCount);
    const newUl = document.createElement('ul');
    const totalCnt = document.querySelector('.total-count');
    totalCnt.innerHTML = `총 ${totalCount} 건`;
    newUl.className = 'course_contentListChange_box';


    // courseData에서 각각의 코스를 처리
    for (const data of courseData) {
        const imageUrl = data.firstimage ? data.firstimage : 'https://example.com/default-image.jpg';
        const courseId = data.contentid;
        const hashtag = getHashtag(data.cat2);

        // 지역 코드에 따라 어느 지역인지 알려줄 코드
        const address = data.areacode;
        const regionName = regionMap[address];


        console.log('data : ', data);

        // 각 코스 항목에 클릭 이벤트를 추가
        newUl.insertAdjacentHTML('beforeend', `
            <li class="course_contentListItem" data-course-id="${courseId}">
                <div class="course_contentListItemImg_box">
                    <div class="course_contentListItemLink" href="javascript:void(0);">
                        <img class="course_contentListItemLinkImg" src="${imageUrl}" alt="">
                    </div>
                </div>
                <div class="course_contentListItemText_box">
                    <div class="course_contentListItemTextLink">
                        <h4 class="course_contentListItemTextLinkTitle">${data.title}</h4>
                    </div>
                    <ul class="course_contentListItemTextList">
                        <li class="course_contentListItemTextListRegion">${regionName}</li>
                        <li class="course_contentListItemTextListHashtag">${hashtag}</li>
                    </ul>
                </div>
                <button id="tourlistInfoBtn" class="tourlist-info-btn" data-course-id="${courseId}">코스 자세히 보기</button>
            </li>
        `);
    }

    courseContent.appendChild(newUl);

    // 코스 항목 클릭 시 이벤트 처리
    const courseItems = document.querySelectorAll('#tourlistInfoBtn');
    console.log('courseItems : ', courseItems);
    courseItems.forEach(item => {
        item.addEventListener('click', () => {
            const courseId = item.getAttribute('data-course-id');
            displayCourseDetails(courseId, courseData);  // courseData와 함께 전달
//            window.location.href = `/tourist/travelcourse-info?contentId=${courseId}`;  // 새로운 페이지로 이동
        });
    });
}








// 상세 정보를 표시할 때, 주소를 경도,    위도로 조회 (카카오 api로 위도경도 표시하기위해 복사한 함수)
async function displayCourseDetails(courseId, courseData) {
    try {
        const course = courseData.find(item => item.contentid === courseId);
        if (!course) {
            console.log('해당 코스를 찾을 수 없습니다.');
            return;
        }
        console.log('course : ' ,course);
        console.log('course.mapy : ',course.mapy);

        const responseContentID = course.contentid;
        const responseInfoData = await getDetailInfo(responseContentID);

        console.log('상세정보 데이터들 : ', responseInfoData.data.response.body.items);


        // 위도, 경도를 가져옵니다
        const latitude = course.mapy;
        const longitude = course.mapx;

        // 위도, 경도를 바탕으로 주소를 조회합니다
        const address = await getAddressFromCoordinates(latitude, longitude);

        // 상세 정보 HTML 생성
        const detailedHtml = `
            <div class="course_details">
                <h3>${course.title}</h3>
                <p>${course.overview}</p>
                <p><strong>주소:</strong> ${address}</p> <!-- 주소를 여기 표시 -->
                <p><strong>전화번호:</strong> ${course.tel || '정보 없음'}</p>
                <p><strong>카테고리:</strong> ${getHashtag(course.cat2) || '정보 없음'}</p>
                <p><strong>지역 코드:</strong> ${course.areaCode || '정보 없음'}</p>
                <img src="${course.firstimage || 'https://example.com/default-image.jpg'}" alt="${course.title}">
            </div>
        `;

        // 상세 정보 표시
        const detailContainer = document.querySelector('.course_details_container');
        detailContainer.innerHTML = detailedHtml;
        detailContainer.classList.remove('hidden'); // 상세 정보 표시
    } catch (error) {
        console.log('Error fetching course details:', error);
        alert('상세 정보를 가져오는데 실패했습니다.');
    }
}


// 주소 정보를 제대로 안주기 때문에 따로 카카오 API를 사용해서 위도경도로 주소를 가져옴
async function getAddressFromCoordinates(lat, lon) {
    console.log('카카오Api에 전달할 위도 경도',lat, lon);

    if (lat === 0 || lon === 0) {
        return '주소 정보가 없습니다';
    }

    try {
        // Kakao API 사용
        const response = await fetch(`https://dapi.kakao.com/v2/local/geo/coord2address.json?x=${lon}&y=${lat}`, {
            headers: {
                Authorization: 'KakaoAK dd93827481a2d3acf5fdb3b0521d48a2' // 카카오 API 키
            }
        });
        const data = await response.json();
        console.log('kakao data : ',data);
        if (data.documents.length > 0) {
            return data.documents[0].address.address_name; // 주소 반환
        } else {
            return '주소를 찾을 수 없습니다'; // 주소가 없을 경우
        }
    } catch (error) {
        console.log('주소 조회 중 오류 발생:', error);
        return '주소 조회 오류'; // 오류 처리
    }
}

// 해시태그를 결정하는 함수
function getHashtag(category) {
    switch (category) {
        case 'C0112': return '#가족코스';
        case 'C0113': return '#나홀로코스';
        case 'C0114': return '#힐링코스';
        case 'C0115': return '#도보코스';
        case 'C0116': return '#캠핑코스';
        case 'C0117': return '#맛코스';
        default: return '#추천코스';
    }
}

// 페이지 버튼을 생성하는 함수
function createPageButtons(totalPages) {
    buttonBox.innerHTML = ''; // 기존 버튼 삭제

    const maxVisibleButtons = 5; // 한 번에 보이는 최대 버튼 수
    const halfVisible = Math.floor(maxVisibleButtons / 2);

    const startPage = Math.max(currentPage - halfVisible, 1);
    const endPage = Math.min(currentPage + halfVisible, totalPages);

    // '처음' 버튼
    if (currentPage > 1) {
        const firstButton = document.createElement('button');
        firstButton.textContent = '처음';
        firstButton.classList.add('pageButton');
        firstButton.addEventListener('click', () => {
            currentPage = 1;
            renderCourses(); // 새 페이지 데이터 렌더링
        });
        buttonBox.appendChild(firstButton);
    }

    // '이전' 버튼
    if (currentPage > 1) {
        const prevButton = document.createElement('button');
        prevButton.textContent = '이전';
        prevButton.classList.add('pageButton');
        prevButton.addEventListener('click', () => {
            currentPage -= 5;
            renderCourses(); // 새 페이지 데이터 렌더링
        });
        buttonBox.appendChild(prevButton);
    }

    // 현재 페이지 기준으로 버튼 생성
    for (let i = startPage; i <= endPage; i++) {
        const button = document.createElement('button');
        button.textContent = i;
        button.classList.add('pageButton');
        if (i === currentPage) {
            button.classList.add('active'); // 현재 페이지 표시
        }
        button.addEventListener('click', () => {
            currentPage = i; // 페이지 변경
            renderCourses(); // 새 페이지 데이터 렌더링
        });
        buttonBox.appendChild(button);
    }

    // '다음' 버튼
    if (currentPage < totalPages) {
        const nextButton = document.createElement('button');
        nextButton.textContent = '다음';
        nextButton.classList.add('pageButton');
        nextButton.addEventListener('click', () => {
            currentPage += 5;
            renderCourses(); // 새 페이지 데이터 렌더링
        });
        buttonBox.appendChild(nextButton);
    }

    // '끝' 버튼
    if (currentPage < totalPages) {
        const lastButton = document.createElement('button');
        lastButton.textContent = '끝';
        lastButton.classList.add('pageButton');
        lastButton.addEventListener('click', () => {
            currentPage = totalPages;
            renderCourses(); // 새 페이지 데이터 렌더링
        });
        buttonBox.appendChild(lastButton);
    }
}


// 코스를 렌더링하는 함수 (데이터 요청, 페이지네이션, HTML 생성)
async function renderCourses() {
    try {
        courseContent.innerHTML = ''; // 기존 내용 제거

        let jsonData;
    jsonData = await getSearchKeyword();
        // 검색어만 입력했을 시에 실행
//        if (currentEncodedData) {
//            jsonData = await getSearchKeyword();
//            if (currentHashtag || currentRegion) {
//                jsonData = await getSearchFilter();
//            }
//            else {
//                jsonData = await getSearchKeyword();
//            }
//        } else {
//            jsonData = await getAreaBasedList();
//        }
        console.log('currentEncodedData(검색어) : ', currentEncodedData);
        console.log('currentHashtag(태그) : ', currentHashtag);
        console.log('currentRegion(지역) : ', currentRegion);

        console.log('jsonData : ', jsonData);

        const courseData = jsonData.data.response.body.items.item;
        const totalPages = Math.ceil(jsonData.data.response.body.totalCount / 10);

        await renderCourseData(jsonData);
        createPageButtons(totalPages);
    } catch (error) {
        console.log('Error:', error);
        buttonBox.innerHTML = '';
        alert('검색된 데이터가 없습니다.');
    }
}

filterButton.addEventListener('click', () => {
    currentPage = 1; // 첫 페이지로 초기화
    currentRegion = regionFilter.value; // 선택된 지역 값 업데이트
    currentHashtag = hashtagFilter.value; // 선택된 해시태그 값 업데이트
    currentEncodedData = encodeURIComponent(searchFilter.value); // 검색어를 URL 인코딩
    renderCourses(); // 필터와 검색어에 맞춘 데이터를 다시 렌더링
});

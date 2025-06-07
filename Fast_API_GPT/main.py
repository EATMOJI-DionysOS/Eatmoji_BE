from fastapi import FastAPI
from personalized_recommend import router as personalized_router
from emoji_recommend import router as recommendation_router

app = FastAPI()

# 다른 모듈에서 정의된 라우터를 가져와서 연결
app.include_router(personalized_router)
app.include_router(recommendation_router)
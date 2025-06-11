from fastapi import APIRouter, HTTPException
from pydantic import BaseModel
from langchain.prompts import PromptTemplate
from langchain.chains import LLMChain
from langchain_openai import ChatOpenAI
import os
import json
from dotenv import load_dotenv
load_dotenv()


router = APIRouter()

# ChatGPT 기반 LLM 초기화
llm = ChatOpenAI(
    temperature=0.7,
    api_key=os.getenv("OPENAI_API_KEY"),
    model="gpt-4o",
    max_tokens=3000,
    request_timeout=60,
)

# emoji → emotion/intensity 변환 로드
with open("emoji2emotion_filtered_baseintensity.json", "r", encoding="utf-8") as f:
    emoji_emotion_data = json.load(f)


# 요청/응답 모델 정의
class EmojiPersonalizedRequest(BaseModel):
    emoji: str
    email: str
    category: list[str]
    flavor: list[str]
    disease: list[str]
    allergy: list[str]
    likedFoods: list[str]

class FoodRecommendation(BaseModel):
    food: str
    reason: str

class RecommendResponse(BaseModel):
    emotion: str
    intensity: float
    recommendations: list[FoodRecommendation]

# GPT 프롬프트 정의
prompt_template = PromptTemplate.from_template("""
너는 사용자의 건강과 선호도를 이해하고, 감정까지 고려해주는 맞춤 음식 추천 도우미야.
5년 이상 된 친구처럼 사용자의 감정에 진심으로 공감하고 사용자에게 적절한 음식 메뉴를 추천해줘.
단답형 대답은 하지말고 대화하듯이 구어체로, 그리고 반말로 말해줘.

사용자 감정: {emotion} (강도: {intensity})
카테고리: {category}
맛 선호: {flavor}
질병: {disease}
알레르기: {allergy}
최근 좋아요 음식: {likedFoods}

이 모든 요소를 종합해, 건강하고 맛있는 음식 1가지를 추천해줘.
각 음식에 대해 간단한 이유도 사용자 선호도 내용을 포함해서서 함께 알려줘.
이유는 2줄 정도로 작성해줘.
형식:
1. 음식이름 - 이유
""")

# LLMChain 생성
chain = LLMChain(llm=llm, prompt=prompt_template)

# API 엔드포인트
@router.post("/gpt/recommend/login", response_model=RecommendResponse)
async def emoji_personalized_recommendation(request: EmojiPersonalizedRequest):
    try:
        # 이모지 → emotion, intensity 변환
        data = emoji_emotion_data.get(request.emoji)
        if not data:
            raise HTTPException(status_code=400, detail="Unsupported emoji")

        emotion = data["emotion"]
        intensity = data["intensity"]

        # GPT에 넘길 입력 데이터
        prompt_input = {
            "emoji": request.emoji,
            "emotion": emotion,
            "intensity": intensity,
            "category": ", ".join(request.category),
            "flavor": ", ".join(request.flavor),
            "disease": ", ".join(request.disease),
            "allergy": ", ".join(request.allergy),
            "likedFoods": ", ".join(request.likedFoods)
        }

        # GPT 호출
        result = chain.run(prompt_input)

        # GPT 응답 파싱
        lines = [line.strip() for line in result.split("\n") if line.strip()]
        recommendations = []
        for line in lines:
            if "." in line and "-" in line:
                try:
                    _, rest = line.split(".", 1)
                    food, reason = rest.split("-", 1)
                    recommendations.append({
                        "food": food.strip(),
                        "reason": reason.strip()
                    })
                except ValueError:
                    continue

        return {
            "emotion": emotion,
            "intensity": intensity,
            "recommendations": recommendations
            }

    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

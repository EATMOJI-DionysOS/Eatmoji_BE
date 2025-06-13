from fastapi import APIRouter, HTTPException
from pydantic import BaseModel
from langchain.prompts import PromptTemplate
from langchain.chains import LLMChain
from langchain_openai import ChatOpenAI
import os
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

# 모델 정의
class PersonalizedRequest(BaseModel):
    email: str
    category: list[str]
    flavor: list[str]
    disease: list[str]
    allergy: list[str]
    likedFoods: list[str]

class FoodRecommendation(BaseModel):
    food: str
    reason: str

class PersonalizedRecommendationResponse(BaseModel):
    recommendations: list[FoodRecommendation]

# Prompt template
personalized_prompt = PromptTemplate.from_template("""
너는 사용자의 건강과 선호도를 이해하는 맞춤 음식 추천 도우미야.

사용자 프로필:
- 카테고리: {category}
- 맛 선호: {flavor}
- 질병: {disease}
- 알레르기: {allergy}

최근 좋아요 누른 음식들:
- {likedFoods}

알레르기나 질병과 상충하지 않는 건강한 음식 1가지를 추천해줘.
각 음식에 대해 두 문장 정도로 간단한 이유도 말해줘.

형식:
1. 음식이름 - 이유

""")

# LLM 체인
personalized_chain = LLMChain(llm=llm, prompt=personalized_prompt)

@router.post("/gpt/recommend/personalized", response_model=PersonalizedRecommendationResponse)
async def personalized_recommendation(request: PersonalizedRequest):
    try:
        # LLM에 입력값 전달
        prompt_input = {
            "category": ", ".join(request.category),
            "flavor": ", ".join(request.flavor),
            "disease": ", ".join(request.disease),
            "allergy": ", ".join(request.allergy),
            "likedFoods": ", ".join(request.likedFoods)
        }
        result = personalized_chain.run(prompt_input)

        # 결과 파싱
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
            "recommendations": recommendations
        }

    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

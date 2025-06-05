from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from langchain.prompts import PromptTemplate
from langchain.chains import LLMChain
from langchain_openai import ChatOpenAI  # 변경된 부분
import os
import json
from dotenv import load_dotenv
import requests

# Load .env file for OpenAI API key
load_dotenv()

# ChatGPT 기반 LLM 초기화
llm = ChatOpenAI(
    temperature=0.7,
    api_key=os.getenv("OPENAI_API_KEY"),  # `openai_api_key` → `api_key`
    model="gpt-4o",
    max_tokens=3000,
    request_timeout=60,
)

# Load emoji2emotion.json
with open("emoji2emotion_filtered_baseintensity.json", "r", encoding="utf-8") as f:
    emoji_emotion_data = json.load(f)

# Initialize FastAPI app
app = FastAPI()

# Define request and response models
class EmotionRequest(BaseModel):
    emoji: str

class FoodRecommendation(BaseModel):
    food: str
    reason: str

class RecommendationResponse(BaseModel):
    emotion: str
    intensity: float
    recommendations: list[FoodRecommendation]

# Define prompt template
prompt_template = PromptTemplate.from_template(
    """
    5년 이상 된 친구처럼 사용자의 감정에 진심으로 공감하고 사용자에게 적절한 음식 메뉴를 추천해줘줘
    단답형 대답은 하지말고 대화하듯이 구어체로 말해줘.
    
    대화의 흐름을 자연스럽게 이어가고, 내내가 보낸 이모지에 대해 진심으로 공감해줘.
    충분히 고민한 것처럼 '음...'이나 '어...'같은 말을 적절히 사용해도 좋아.
    사용자가 보낸 이모지에 따라 감정을 읽어내고, 그 감정의 강도에 따라 추천하는 음식을 다르게해줘.

    지금 사용자는 {emotion}한 기분이고, 그 강도는 {intensity} 정도야 (0에서 1 사이).
    감정이 강할수록 진심을 담고 위로하듯이, 감정이 약하면 가볍고 유쾌하게 말해줘.
    어울릴만한 음식 3가지를 추천해주고, 각 음식에 대해 두 문장 정도로 간단한 이유도 덧붙여줘.

    형식:
    1. 음식이름 - 이유
    2. 음식이름 - 이유
    3. 음식이름 - 이유
    """
)

# LLM 체인 구성
chain = LLMChain(llm=llm, prompt=prompt_template)

@app.post("/gpt/recommendation", response_model=RecommendationResponse)
async def get_recommendation(request: EmotionRequest):
    try:

        # 이모지에서 감정 및 강도 추출
        data = emoji_emotion_data.get(request.emoji)
        if not data:
            raise HTTPException(status_code=400, detail="Unsupported emoji")

        emotion = data["emotion"]
        intensity = data["intensity"]

        # LLM 호출
        result = chain.run({"emotion": emotion, "intensity": intensity})

        # 응답 파싱
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

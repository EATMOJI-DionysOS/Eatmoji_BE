from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from langchain.prompts import PromptTemplate
from langchain_openai import OpenAI
from langchain.chains import LLMChain
import os
import json
from dotenv import load_dotenv

# Load .env file for OpenAI API key
load_dotenv()
llm = OpenAI(temperature=0.7, openai_api_key=os.getenv("OPENAI_API_KEY"))

# Load emoji2emotion.json
with open("emoji2emotion_filtered_baseintensity.json", "r", encoding="utf-8") as f:
    emoji_emotion_data = json.load(f)

# Initialize FastAPI app
app = FastAPI()

# Define request and response models
class EmotionRequest(BaseModel):
    emoji: str  # e.g., "😭", "😄"

class FoodRecommendation(BaseModel):
    food: str
    reason: str

class RecommendationResponse(BaseModel):
    emotion: str
    intensity: float
    recommendations: list[FoodRecommendation]

# Define prompt template with friendly tone and intensity-based nuance
prompt_template = PromptTemplate.from_template(
    """
    너는 감정에 따라 음식을 추천해주는 친구야.
    5년 이상 된 친구처럼 대화체로 대화해줘. 단답형 대답은 절대 하지마.
    사용자가 보낸 이모지에 따라 감정을 읽어내고, 그 감정의 강도에 따라 추천하는 음식을 달리해줘.
    
    지금 사용자는 {emotion}한 기분이고, 그 강도는 {intensity} 정도야 (0에서 1 사이).
    감정이 강할수록 진심을 담고 위로하듯이, 감정이 약하면 가볍고 유쾌하게 말해줘.
    어울릴만한 음식 3가지를 추천해주고, 각 음식에 대해 한 문장 정도로 간단한 이유도 덧붙여줘.
    이유는 최소 15자 이상, 최대 30자 이내로 해줘.
    추천하는 음식은 한국에서 자주 먹는 음식으로만 해줘.
    
    예를 들면 한식, 양식, 대중화된 일식, 대중화된 중식, 배달음식 같은 실제 사람들이 자주 먹는 음식들로만 추천해. 
    특이하거나 외국 음식, 생소한 요리는 절대 추천하지 마.
    형식:
    1. 음식 - 이유
    2. 음식 - 이유
    3. 음식 - 이유
    """
)

# Create the LLM chain
chain = LLMChain(llm=llm, prompt=prompt_template)

# Define API endpoint
@app.post("/gpt/recommendation", response_model=RecommendationResponse)
async def get_recommendation(request: EmotionRequest):
    try:
        # Extract emotion and intensity from emoji
        data = emoji_emotion_data.get(request.emoji)
        if not data:
            raise HTTPException(status_code=400, detail="Unsupported emoji")

        emotion = data["emotion"]
        intensity = data["intensity"]

        # Call GPT chain with emotion and intensity
        result = chain.run({"emotion": emotion, "intensity": intensity})

        # Parse GPT result
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

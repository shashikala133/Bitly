import { useQuery } from "@tanstack/react-query";
import api from "../api/api"


export const useFetchMyShortUrls = ({ token, onError }) => {
  return useQuery({
    queryKey: ["my-shortenurls"],
    queryFn: async () => {
      const response = await api.get("/api/urls/my-urls", {
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json",
          Authorization: "Bearer " + token,
        },
      });
      return response;
    },
    select: (data) => {
      return data.data.sort(
        (a, b) => new Date(b.createdDate) - new Date(a.createdDate)
      );
    },
    onError,
    staleTime: 5000,
  });
};

export const useFetchTotalClicks = ({ token, onError }) => {
  return useQuery({
    queryKey: ["url-totalclick"],
    queryFn: async () => {
      const response = await api.get(
        "/api/urls/analytics?startDate=2024-01-01&endDate=2028-12-31",
        {
          headers: {
            "Content-Type": "application/json",
            Accept: "application/json",
            Authorization: "Bearer " + token,
          },
        }
      );
      return response;
    },
    select: (data) => {
      const convertToArray = Object.keys(data.data).map((key) => ({
        clickDate: key,
        count: data.data[key],
      }));
      return convertToArray;
    },
    onError,
    staleTime: 5000,
  });
};
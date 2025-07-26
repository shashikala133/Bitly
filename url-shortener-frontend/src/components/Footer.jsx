
import React from "react";
import { FaFacebook, FaTwitter, FaInstagram, FaLinkedin } from "react-icons/fa";

const Footer = () => {
  return (
    <footer className="bg-gray-100 text-gray-700 py-6">
      <div className="max-w-6xl mx-auto px-4 flex flex-col lg:flex-row justify-between items-center gap-4 text-sm">
        <div className="text-center lg:text-left">
          <h2 className="text-xl font-semibold mb-1">Linklytics</h2>
          <p>Simplifying URL shortening for efficient sharing</p>
        </div>

        <p className="text-center lg:text-left">
          &copy; Shashikala 2025 Linklytics. All rights reserved.
        </p>

        <div className="flex space-x-4">
          <a href="#"><FaFacebook /></a>
          <a href="#"><FaTwitter /></a>
          <a href="#"><FaInstagram /></a>
          <a href="#"><FaLinkedin /></a>
        </div>
      </div>
    </footer>
  );
};

export default Footer;
